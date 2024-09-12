package domain.gateway

import domain.entity.Store

interface IStoresGateway {
    suspend fun getAllStores(): List<Store>
}