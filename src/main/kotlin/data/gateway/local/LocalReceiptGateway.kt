package data.gateway.local

import data.util.getData
import domain.entity.*
import domain.gateway.IReceiptGateway
import domain.gateway.local.ILocalSetupStoreGateway
import domain.util.EmptyDataException
import domain.util.NotFoundException
import domain.util.UnknownErrorException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.math.BigDecimal

class LocalReceiptGateway(
    private val localSetupStoreGateway: ILocalSetupStoreGateway,
) : IReceiptGateway {
    private val _setupStore = CoroutineScope(Dispatchers.IO).async {
        localSetupStoreGateway.getSetupStore()
    }

    override suspend fun getAllReceipts(storeId: String, dateFrom: String, dateTo: String): List<Receipt> {
        return try {
            val list = mutableListOf<Receipt>()
            val setupStore = _setupStore.await()
            val data = getData(
                "SELECT \n" +
                        "    pd.INVC_ID,\n" +
                        "    pd.Recipt_No,\n" +
                        "    pd.dateTimeIssued,\n" +
                        "    pd.uuid,\n" +
                        "    pd.submissionid,\n" +
                        "    pd.status,\n" +
                        "    pt.Perc,\n" +
                        "    pd.Store_ID,\n" +
                        "    pd.totalAmount,\n" +
                        "    pd.Tax As TotalTax,\n" +
                        "    pd.currency,\n" +
                        "    pd.sOrderNameCode,\n" +
                        "    pd.orderdeliveryMode,\n" +
                        "    pd.grossWeight,\n" +
                        "    pd.netWeight,\n" +
                        "    pd.receiptType,\n" +
                        "    pd.typeVersion,\n" +
                        "    pd.totalCommercialDiscount,\n" +
                        "    pd.totalSales,\n" +
                        "    pd.feesAmount,\n" +
                        "    pd.netAmount,\n" +
                        "    \n" +
                        "    pt.Tax_Type,\n" +
                        "    pt.Tax_SubType,\n" +
                        "    pt.Value,\n" +
                        "\n" +
                        "    pb.type AS BuyerType,\n" +
                        "    pb.id AS BuyerId,\n" +
                        "    pb.name AS BuyerName,\n" +
                        "    pb.mobileNumber AS BuyerMobileNumber,\n" +
                        "\n" +
                        " (select (COALESCE(last_uuid,'')) from dbo.Stores) as last_uuid,\n" +
                        "\n" +
                        "    pl.description,\n" +
                        "    pl.itemType,\n" +
                        "    pl.itemCode,\n" +
                        "    pl.unitType,\n" +
                        "    pl.quantity,\n" +
                        "    pl.unitPrice,\n" +
                        "    pl.internalCode,\n" +
                        "    pl.totalSale,\n" +
                        "    pl.netSale,\n" +
                        "    pl.Perc_Tax,\n" +
                        "    pl.Tax,\n" +
                        "    pl.total,\n" +
                        "    pl.itemDiscountData,\n" +
                        "    pl.commercialDiscountData\n" +
                        "\n" +
                        "FROM \n" +
                        "    dbo.pos_document pd\n" +
                        "LEFT JOIN \n" +
                        "    dbo.Pos_Tax pt ON pd.INVC_ID = pt.INVC_ID\n" +
                        "LEFT JOIN \n" +
                        "    dbo.Pos_buyer pb ON pd.INVC_ID = pb.INVC_ID\n" +
                        "LEFT JOIN \n" +
                        "    dbo.pos_liens pl ON pd.INVC_ID = pl.INVC_ID\n" +
                        "WHERE \n" +
                        "    pd.Store_ID = $storeId \n" +
                        "   and pd.dateTimeIssued between '$dateFrom 00:00:00.000' and '$dateTo 23:59:59.000'\n"
            )
            if (data.isSuccess) {
                val receiptsMap = mutableMapOf<String, MutableList<Map<String, Any?>>>()
                data.getOrNull()?.forEach { row ->
                    val invoiceId = row["INVC_ID"]?.toString() ?: ""
                    if (!receiptsMap.containsKey(invoiceId)) {
                        receiptsMap[invoiceId] = mutableListOf()
                    }
                    receiptsMap[invoiceId]?.add(row)
                } ?: throw EmptyDataException("Empty Invoices")
                receiptsMap.map { (invoiceId, rows) ->
                    val firstRow = rows.first()
                    val items = rows.map { row ->
                        ItemData(
                            description = row["description"]?.toString() ?: "",
                            itemType = row["itemType"]?.toString() ?: "",
                            itemCode = row["itemCode"]?.toString() ?: "",
                            unitType = row["unitType"]?.toString() ?: "",
                            quantity = row["quantity"]?.toString()?.toBigDecimalOrNull()
                                ?.setScale(5, BigDecimal.ROUND_HALF_UP)
                                ?: BigDecimal(0.0),
                            unitPrice = row["unitPrice"]?.toString()?.toBigDecimalOrNull()
                                ?.setScale(5, BigDecimal.ROUND_HALF_UP) ?: BigDecimal(
                                0.0
                            ),
                            internalCode = row["internalCode"]?.toString() ?: "",
                            totalSale = row["totalSale"]?.toString()?.toBigDecimalOrNull()
                                ?.setScale(5, BigDecimal.ROUND_HALF_UP) ?: BigDecimal(
                                0.0
                            ),
                            netSale = row["netSale"]?.toString()?.toBigDecimalOrNull()
                                ?.setScale(5, BigDecimal.ROUND_HALF_UP) ?: BigDecimal(0.0),
                            total = row["total"]?.toString()?.toBigDecimalOrNull()
                                ?.setScale(5, BigDecimal.ROUND_HALF_UP)
                                ?: BigDecimal(0.0),
                            itemDiscountData = emptyList(),
                            commercialDiscountData = listOf(
                                CommercialDiscountData(
                                    amount = row["commercialDiscountData"]?.toString()?.toBigDecimalOrNull()
                                        ?.setScale(5, BigDecimal.ROUND_HALF_UP)
                                        ?: BigDecimal(0.0),
                                    description = "خصم",
                                    rate = calculateDiscountPercentage(
                                        row["totalSale"]?.toString()?.toBigDecimalOrNull()
                                            ?.setScale(5, BigDecimal.ROUND_HALF_UP) ?: BigDecimal(
                                            0.0
                                        ),
                                        row["commercialDiscountData"]?.toString()?.toBigDecimalOrNull()
                                            ?.setScale(5, BigDecimal.ROUND_HALF_UP)
                                            ?: BigDecimal(0.0)
                                    )
                                )
                            ),
                            taxableItems = listOf(
                                TaxableItem(
                                    taxType = row["Tax_Type"]?.toString() ?: "",
                                    amount = row["Tax"]?.toString()?.toBigDecimalOrNull()
                                        ?.setScale(5, BigDecimal.ROUND_HALF_UP)
                                        ?: BigDecimal(0.0),
                                    subType = row["Tax_SubType"]?.toString() ?: "",
                                    rate = row["Perc_Tax"]?.toString()?.toDoubleOrNull() ?: 0.0
                                )
                            ),
                        )
                    }
                    list.add(
                        Receipt(
                            id = invoiceId,
                            header = Header(
                                dateTimeIssued = firstRow["dateTimeIssued"]?.toString() ?: "",
                                receiptNumber = firstRow["Recipt_No"]?.toString() ?: "",
                                uuid = firstRow["uuid"]?.toString() ?: "",
                                previousUUID = firstRow["last_uuid"]?.toString() ?: "",
                                referenceOldUUID = "",
                                currency = firstRow["currency"]?.toString() ?: "",
                                exchangeRate = 0,
                                sOrderNameCode = firstRow["sOrderNameCode"]?.toString() ?: "",
                                grossWeight = firstRow["grossWeight"]?.toString()?.toIntOrNull() ?: 0,
                                netWeight = firstRow["netWeight"]?.toString()?.toIntOrNull() ?: 0,
                            ),
                            documentType = DocumentType(
                                receiptType = firstRow["receiptType"]?.toString() ?: "",
                                typeVersion = firstRow["typeVersion"]?.toString() ?: "",
                            ),
                            seller = Seller(
                                rin = setupStore.rin,
                                companyTradeName = setupStore.companyTradeName,
                                branchCode = setupStore.branchCode,
                                branchAddress = BranchAddress(
                                    country = setupStore.country,
                                    governate = setupStore.governance,
                                    regionCity = setupStore.regionCity,
                                    street = setupStore.street,
                                    buildingNumber = setupStore.buildingNumber,
                                    postalCode = setupStore.postalCode,
                                    floor = setupStore.floor,
                                    room = setupStore.room,
                                    landmark = setupStore.landmark,
                                    additionalInformation = setupStore.additionalInformation,
                                ),
                                deviceSerialNumber = setupStore.deviceSerialNumber,
                                syndicateLicenseNumber = setupStore.syndicateLicenseNumber,
                                activityCode = setupStore.activityCode,
                            ),
                            buyer = Buyer(
                                type = firstRow["BuyerType"]?.toString() ?: "",
                                id = firstRow["BuyerId"]?.toString() ?: "",
                                name = firstRow["BuyerName"]?.toString() ?: "",
                                mobileNumber = firstRow["BuyerMobileNumber"]?.toString() ?: "",
                            ),
                            itemData = items,
                            totalSales = firstRow["totalSales"]?.toString()?.toBigDecimalOrNull()
                                ?.setScale(5, BigDecimal.ROUND_HALF_UP)
                                ?: BigDecimal(0.0),
                            totalCommercialDiscount = firstRow["totalCommercialDiscount"]?.toString()
                                ?.toBigDecimalOrNull() ?: BigDecimal(0.0),
                            netAmount = firstRow["netAmount"]?.toString()?.toBigDecimalOrNull()

                                ?: BigDecimal(0.0),
                            feesAmount = firstRow["feesAmount"]?.toString()?.toDoubleOrNull() ?: 0.0,
                            totalAmount = firstRow["totalAmount"]?.toString()?.toBigDecimalOrNull()
                                ?.setScale(5, BigDecimal.ROUND_HALF_UP)
                                ?: BigDecimal(0.0),
                            taxTotals = listOf(
                                TaxTotal(
                                    taxType = firstRow["Tax_Type"]?.toString() ?: "",
                                    amount = firstRow["TotalTax"]?.toString()?.toBigDecimalOrNull()
                                        ?.setScale(5, BigDecimal.ROUND_HALF_UP)
                                        ?: BigDecimal(0.0),
                                )
                            ),
                            paymentMethod = "C",
                        )
                    )
                }
                list
            } else throw NotFoundException("invoices not found")
        } catch (e: Exception) {
            throw UnknownErrorException(e.message)
        }
    }
}

private fun calculateDiscountPercentage(originalPrice: BigDecimal, discountPrice: BigDecimal): Double {
    if (originalPrice.toDouble() == 0.0) return 0.0
    val result = (discountPrice.toDouble() / originalPrice.toDouble()) * 100.0
    return if (result.isNaN()) 0.0 else result
}