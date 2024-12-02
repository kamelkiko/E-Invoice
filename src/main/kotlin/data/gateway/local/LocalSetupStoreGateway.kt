package data.gateway.local

import data.local.mapper.toStoreCollection
import data.local.mapper.toStoreSetup
import data.local.mapper.toUser
import data.local.mapper.toUserCollection
import data.local.model.StoreCollection
import data.local.model.UserCollection
import domain.entity.StoreSetup
import domain.gateway.local.ILocalSetupStoreGateway
import domain.util.NotFoundException
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query

class LocalSetupStoreGateway(private val realm: Realm) : ILocalSetupStoreGateway {
    override suspend fun getSetupStore(): StoreSetup {
        return realm.query<StoreCollection>().first().find()?.toStoreSetup()
            ?: throw NotFoundException("Not Found")
    }

    override suspend fun addSetupStore(setup: StoreSetup): Boolean {
        realm.write {
            copyToRealm(setup.toStoreCollection(), updatePolicy = UpdatePolicy.ALL)
        }
        return true
    }

    override suspend fun updateSetupStore(setup: StoreSetup): Boolean {
        realm.query<StoreCollection>("$ID == $CONFIGURATION_ID").first().find()?.apply {
            rin = setup.rin
            companyTradeName = setup.companyTradeName
            branchCode = setup.branchCode
            country = setup.country
            governate = setup.governance
            regionCity = setup.regionCity
            street = setup.street
            buildingNumber = setup.buildingNumber
            postalCode = setup.postalCode
            floor = setup.floor
            room = setup.room
            landmark = setup.landmark
            additionalInformation = setup.additionalInformation
            deviceSerialNumber = setup.deviceSerialNumber
            syndicateLicenseNumber = setup.syndicateLicenseNumber
            activityCode = setup.activityCode
            posSerial = setup.posSerial
            posOsVersion = setup.posOsVersion
            clientId = setup.clientId
            clientSecret = setup.clientSecret
        }
        return true
    }


    private companion object {
        private const val CONFIGURATION_ID = 0
        private const val ID = "id"
    }
}