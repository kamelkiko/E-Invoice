package domain.gateway.local

import domain.entity.StoreSetup

interface ILocalSetupStoreGateway {
    suspend fun getSetupStore(): StoreSetup
    suspend fun addSetupStore(setup: StoreSetup): Boolean
    suspend fun updateSetupStore(setup: StoreSetup): Boolean
}