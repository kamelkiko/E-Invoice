package domain.usecase

import domain.entity.StoreSetup
import domain.gateway.local.ILocalSetupStoreGateway

class ManageSetupStoreUseCase(
    private val localSetupStoreGateway: ILocalSetupStoreGateway
) {

    suspend fun createSetup(setup: StoreSetup, id: Int): Boolean {
        return localSetupStoreGateway.addSetupStore(setup)
    }

    suspend fun getSetup(): StoreSetup {
        return localSetupStoreGateway.getSetupStore()
    }

    suspend fun updateSetup(setup: StoreSetup, id: Int): Boolean {
        return localSetupStoreGateway.updateSetupStore(setup)
    }
}