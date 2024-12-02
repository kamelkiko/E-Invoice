package data.local.mapper

import data.local.model.StoreCollection
import domain.entity.StoreSetup

fun StoreCollection.toStoreSetup(): StoreSetup {
    return StoreSetup(
        id = this.id.toString(),
        rin = rin,
        companyTradeName = companyTradeName,
        branchCode = branchCode,
        country = country,
        governance = governate,
        regionCity = regionCity,
        street = street,
        buildingNumber = buildingNumber,
        postalCode = postalCode,
        floor = floor,
        room = room,
        landmark = landmark,
        additionalInformation = additionalInformation,
        deviceSerialNumber = deviceSerialNumber,
        syndicateLicenseNumber = syndicateLicenseNumber,
        activityCode = activityCode,
        posSerial = posSerial,
        posOsVersion = posOsVersion,
        clientId = clientId,
        clientSecret = clientSecret,
    )
}

fun StoreSetup.toStoreCollection(): StoreCollection {
    return StoreCollection().apply {
        id = this@toStoreCollection.id.toIntOrNull() ?: 0
        rin = this@toStoreCollection.rin
        companyTradeName = this@toStoreCollection.companyTradeName
        branchCode = this@toStoreCollection.branchCode
        country = this@toStoreCollection.country
        governate = this@toStoreCollection.governance
        regionCity = this@toStoreCollection.regionCity
        street = this@toStoreCollection.street
        buildingNumber = this@toStoreCollection.buildingNumber
        postalCode = this@toStoreCollection.postalCode
        floor = this@toStoreCollection.floor
        room = this@toStoreCollection.room
        landmark = this@toStoreCollection.landmark
        additionalInformation = this@toStoreCollection.additionalInformation
        deviceSerialNumber = this@toStoreCollection.deviceSerialNumber
        syndicateLicenseNumber = this@toStoreCollection.syndicateLicenseNumber
        activityCode = this@toStoreCollection.activityCode
        posSerial = this@toStoreCollection.posSerial
        posOsVersion = this@toStoreCollection.posOsVersion
        clientId = this@toStoreCollection.clientId
        clientSecret = this@toStoreCollection.clientSecret
    }
}