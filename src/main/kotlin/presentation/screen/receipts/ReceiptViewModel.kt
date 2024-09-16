package presentation.screen.receipts

import cafe.adriel.voyager.core.model.screenModelScope
import com.vladsch.kotlin.jdbc.sqlQuery
import com.vladsch.kotlin.jdbc.usingDefault
import data.util.AppConstants
import data.util.HistoryData
import domain.entity.DataWrapper
import domain.entity.Receipt
import domain.usecase.ManageReceiptUseCase
import domain.usecase.SendReceiptUseCase
import kotlinx.coroutines.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import presentation.base.BaseViewModel
import presentation.base.ErrorState
import util.getDateNow
import java.nio.charset.StandardCharsets.UTF_8
import java.security.MessageDigest
import kotlin.math.ceil

class ReceiptViewModel(
    private val manageReceipts: ManageReceiptUseCase,
    private val sendReceipt: SendReceiptUseCase,
) : BaseViewModel<ReceiptUiState, ReceiptUiEffect>(ReceiptUiState()), ReceiptInteractionListener {

    private val json by lazy {
        Json {
            prettyPrint = true
            allowSpecialFloatingPointValues = true
            ignoreUnknownKeys = true
        }
    }

    private var limitJob: Job? = null

    override val viewModelScope: CoroutineScope get() = screenModelScope

    private fun getReceipts() {
        updateState { it.copy(isLoading = true, hasConnection = true, selectedReceipts = emptyList()) }
        tryToExecute(
            {
                state.value.run {
                    val result = manageReceipts.getAllReceipts(AppConstants.storeId, startDate, endDate)
                    val startIndex = (currentPage - 1) * specifiedReceipts
                    val endIndex = startIndex + specifiedReceipts
                    val numberOfPages = if (result.isNotEmpty())
                        ceil(result.size / (specifiedReceipts * 1.0)).toInt() else 0
                    DataWrapper(
                        totalPages = numberOfPages,
                        numberOfResult = if (result.isNotEmpty()) result.size else 0,
                        result = if (result.isEmpty()) emptyList() else
                            result.subList(startIndex, endIndex.coerceAtMost(result.size))
                    )
                }
            },
            ::onGetReceiptSuccessfully,
            ::onError
        )
    }

    private fun onGetReceiptSuccessfully(receipts: DataWrapper<Receipt>) {
        updateState {
            it.copy(
                pageInfo = receipts.toDetailsUiState(),
                isLoading = false,
                hasConnection = true,
                selectedReceipts = emptyList()
            )
        }
        if (state.value.currentPage > state.value.pageInfo.totalPages && receipts.toDetailsUiState().data.isNotEmpty()) {
            onPageClick(state.value.pageInfo.totalPages)
        }
    }

    private fun onError(error: ErrorState) {
        updateState { it.copy(isLoading = false, isLoadingSend = false) }
        when (error) {
            is ErrorState.NetworkError -> {
                updateState {
                    it.copy(
                        hasConnection = false,
                        isSnackBarVisible = true,
                        snackBarTitle = error.message ?: "",
                    )
                }
            }

            is ErrorState.ValidationNetworkError -> {
                updateState {
                    it.copy(
                        isSnackBarVisible = true,
                        snackBarTitle = error.message ?: "",
                        selectedReceipts = emptyList(),
                    )
                }
            }

            is ErrorState.UnAuthorized -> {
                updateState {
                    it.copy(
                        isSnackBarVisible = true,
                        snackBarTitle = error.message ?: "",
                    )
                }
            }

            is ErrorState.NotFound -> {
                updateState {
                    it.copy(
                        isSnackBarVisible = true,
                        snackBarTitle = error.message ?: "",
                    )
                }
            }

            is ErrorState.UnknownError -> {
                updateState {
                    it.copy(
                        isSnackBarVisible = true,
                        snackBarTitle = error.message ?: "",
                        selectedReceipts = emptyList(),
                    )
                }
            }

            else -> updateState {
                it.copy(
                    isSnackBarVisible = true,
                    snackBarTitle = "Something happened please try again!",
                    selectedReceipts = emptyList(),
                )
            }
        }

    }

    override fun onItemsIndicatorChange(itemPerPage: Int) {
        updateState { it.copy(specifiedReceipts = itemPerPage) }
        launchLimitJob()
    }

    private fun launchLimitJob() {
        limitJob?.cancel()
        limitJob = launchDelayed(300L) { getReceipts() }
    }

    override fun onPageClick(pageNumber: Int) {
        updateState { it.copy(currentPage = pageNumber, unSelect = false, selectedReceipts = emptyList()) }
        getReceipts()
    }

    override fun onChooseStartDateInputChange(date: String) {
        updateState { it.copy(startDate = date) }
    }

    override fun onChooseEndDateInputChange(date: String) {
        updateState { it.copy(endDate = date) }
    }

    override fun onDismissStartDate() {
        updateState { it.copy(dateFromIsVisible = false) }
    }

    override fun onDismissEndDate() {
        updateState { it.copy(dateToIsVisible = false) }
    }

    override fun onShowStartDate() {
        updateState { it.copy(dateFromIsVisible = true) }
    }

    override fun onShowEndDate() {
        updateState { it.copy(dateToIsVisible = true) }
    }

    override fun onEditTaxiClicked(itemId: Long) {
        val currentTaxisState = state.value.pageInfo.data
        val selectedItemState = currentTaxisState.first { (it.header.receiptNumber.toLongOrNull() ?: 0L) == itemId }
        val updatedTaxiState = selectedItemState.copy(isChecked = selectedItemState.isChecked.not())
        updateState {
            it.copy(
                pageInfo = state.value.pageInfo.copy(data = currentTaxisState.toMutableList()
                    .apply { set(indexOf(selectedItemState), updatedTaxiState) }),
                selectedReceipts = state.value.selectedReceipts.toMutableList().apply {
                    if (updatedTaxiState.isChecked) {
                        add(updatedTaxiState)
                    } else {
                        removeIf { r -> (r.header.receiptNumber.toLongOrNull() ?: 0L) == itemId }
                    }
                }
            )
        }
    }

    override fun onClickSearch() {
        getReceipts()
    }

    override fun onClickSelectAll() {
        if (state.value.pageInfo.data.isNotEmpty()) {
            state.value.pageInfo.data.forEach { selectedItemState ->
                val currentTaxisState = state.value.pageInfo.data
                val updatedTaxiState =
                    if (state.value.unSelect)
                        selectedItemState.copy(isChecked = false)
                    else
                        selectedItemState.copy(isChecked = true)
                updateState {
                    it.copy(
                        pageInfo = state.value.pageInfo.copy(data = currentTaxisState.toMutableList()
                            .apply { set(indexOf(selectedItemState), updatedTaxiState) }),
                        selectedReceipts = state.value.selectedReceipts.toMutableList().apply {
                            if (updatedTaxiState.isChecked) {
                                add(updatedTaxiState)
                            } else {
                                removeIf { r ->
                                    (r.header.receiptNumber.toLongOrNull()
                                        ?: 0L) == (selectedItemState.header.receiptNumber.toLongOrNull() ?: 0L)
                                }
                            }
                        }
                    )
                }
            }
            updateState { it.copy(unSelect = state.value.unSelect.not()) }
        }
    }

    override fun onClickSend() {
        updateState { it.copy(isLoadingSend = true) }
        tryToExecute(
            function = {
                val receipts = state.value.selectedReceipts.sortedBy { it.header.receiptNumber }.map {
                    it.toEntity()
                }.map { receiptDetailsUiState ->
                    val temp = cleanDocument(receiptDetailsUiState)
                    val encryptedUUID = signDocument(temp, false)
                    receiptDetailsUiState.copy(header = receiptDetailsUiState.header.copy(uuid = encryptedUUID))
                }
                sendReceipt(receipts)
            },
            onSuccess = { submit ->
                updateState { it.copy(isLoadingSend = false) }
                viewModelScope.launch(Dispatchers.Default) {
                    submit.acceptedDocuments?.forEach { document ->
                        try {
                            usingDefault { session ->
                                val query0 = sqlQuery("DISABLE TRIGGER FposCheckIU ON dbo.FposCheck;")
                                session.execute(query0)
                                val query1 =
                                    sqlQuery("update dbo.FposCheck set UUID='${document.uuid}',Submit_UUID='${submit.submissionId}' where SerialNumber=${document.receiptNumber} and DateMade='${state.value.selectedReceipts.first { document.receiptNumber == it.header.receiptNumber }.header.dateTimeIssued}';")
                                session.execute(query1)
                                val query2 = sqlQuery("update dbo.Stores set last_uuid = '${document.uuid}';")
                                session.execute(query2)
                                val query3 = sqlQuery("ENABLE TRIGGER FposCheckIU ON dbo.FposCheck;")
                                session.execute(query3)
                                AppConstants.historyData.add(
                                    HistoryData(
                                        uuid = document.uuid ?: "",
                                        submitUUID = submit.submissionId ?: "",
                                        dateTimeIssued = state.value.selectedReceipts.first { document.receiptNumber == it.header.receiptNumber }.header.dateTimeIssued,
                                        dateTimeReceived = getDateNow().toString(),
                                        receiptNumber = document.receiptNumber ?: "",
                                        taxTotals = state.value.selectedReceipts.first { document.receiptNumber == it.header.receiptNumber }.taxTotals.sumOf { it.amount },
                                        totalSales = state.value.selectedReceipts.first { document.receiptNumber == it.header.receiptNumber }.totalSales,
                                        totalCommercialDiscount = state.value.selectedReceipts.first { document.receiptNumber == it.header.receiptNumber }.totalCommercialDiscount,
                                        netAmount = state.value.selectedReceipts.first { document.receiptNumber == it.header.receiptNumber }.netAmount,
                                        feesAmount = state.value.selectedReceipts.first { document.receiptNumber == it.header.receiptNumber }.feesAmount,
                                        totalAmount = state.value.selectedReceipts.first { document.receiptNumber == it.header.receiptNumber }.totalAmount,
                                        storeName = AppConstants.storeName,
                                        status = "Unknown",
                                    )
                                )
                            }
                        } catch (e: Exception) {
                            updateState {
                                it.copy(
                                    isSnackBarVisible = true,
                                    snackBarTitle = e.message ?: "",
                                )
                            }
                        }
                    }
                    submit.rejectedDocuments.forEach { rejectedDocument ->
                        val errorMessages =
                            rejectedDocument.error.details.joinToString { it.message ?: "Something went wrong" }
                        updateState {
                            it.copy(
                                isSnackBarVisible = true,
                                snackBarTitle = "receipt ${rejectedDocument.receiptNumber} rejected because $errorMessages",
                            )
                        }
                        delay(1500L)
                    }
                    updateState {
                        it.copy(
                            isSnackBarSuccessVisible = true,
                            snackBarSuccessTitle = "Receipts sent successfully, please check your history",
                            selectedReceipts = emptyList()
                        )
                    }
                    updateState { it.copy(selectedReceipts = emptyList()) }
                    getReceipts()
                }
            },
            onError = ::onError
        )
    }

    private fun serialize(request: JsonObject): String {
        return serializeToken("ITEMDATA", request)
    }

    // Recursive function to serialize the document according to the requirements
    private fun serializeToken(name: String? = null, request: JsonElement): String {
        var serialized = ""

        when (request) {
            is JsonObject -> {
                request.entries.forEach { (key, value) ->
                    // Uppercase property names and serialize the value
                    serialized += "\"${key.uppercase()}\""
                    serialized += serializeToken(
                        if (key.uppercase() in listOf(
                                "ITEMDATA",
                                "COMMERCIALDISCOUNTDATA",
                                "TAXABLEITEMS",
                                "ITEMDISCOUNTDATA",
                                "TAXTOTALS",
                            )
                        ) key.uppercase() else "ITEMDATA",
                        value
                    )
                }
            }

            is JsonArray -> {
                request.forEach { item ->
                    name?.let {
                        serialized += "\"${name.uppercase()}\""
                    }

                    serialized += serializeToken(null, item)
                }
            }

            is JsonPrimitive -> {
                serialized += if (request.isString) {
                    // Enclose simple values (strings) in double quotes
                    "\"${request.content.replace("\n", "\\n")}\""
                } else {
                    // Use raw primitive value (e.g., numbers) as is
                    "\"${request}\""
                }
            }

            else -> {}
        }

        return serialized
    }

    private fun computeSha256Hash(normalizedText: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(normalizedText.toByteArray(UTF_8))
        return bytes.joinToString("") { "%02x".format(it) } // Convert byte array to hex string
    }

    private fun signDocument(document: String, canonical: Boolean = false): String {
        val jsonObject = Json.decodeFromString<JsonObject>(document)

        val canonicalString = serialize(jsonObject)

        return if (canonical) {
            canonicalString
        } else {
            computeSha256Hash(canonicalString)
        }
    }

    private inline fun <reified T> cleanDocument(data: T): String {
        return json.encodeToString(data)
    }


    override fun onRetry() {
        getReceipts()
    }

    override fun onSnackBarDismiss() {
        updateState {
            it.copy(
                isSnackBarVisible = false
            )
        }
    }

    override fun onSnackBarSuccessDismiss() {
        updateState {
            it.copy(
                isSnackBarSuccessVisible = false
            )
        }
    }
}

private fun canConvertToInt(number: Double): Boolean {
    return number % 1 == 0.0
}