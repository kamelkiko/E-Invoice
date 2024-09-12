package data.gateway.local

import data.util.getData
import domain.entity.Store
import domain.gateway.IStoresGateway
import domain.gateway.local.ILocalConfigurationGateway
import domain.util.EmptyDataException
import domain.util.NotFoundException
import domain.util.UnknownErrorException

class StoresGateway(
    private val localConfigurationGateway: ILocalConfigurationGateway
) : IStoresGateway {
    override suspend fun getAllStores(): List<Store> {
        return try {
            val list = mutableListOf<Store>()
            val data = getData("select * from dbo.Store")
            if (data.isSuccess) {
                data.getOrNull()?.forEach { row ->
                    list.add(
                        Store(
                            id = row["Store_ID"]?.toString()?.toIntOrNull() ?: 0,
                            name = row["Name"]?.toString() ?: "",
                        )
                    )
                } ?: throw EmptyDataException("Empty store")
                list ?: throw NotFoundException("Store not found")
            } else throw NotFoundException("Store not found")
        } catch (e: Exception) {
            throw UnknownErrorException(e.message)
        }
    }
}