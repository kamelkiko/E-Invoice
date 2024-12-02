package data.local.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class StoreCollection : RealmObject {
    @PrimaryKey
    var id: Int = 0
    var rin: String = ""
    var companyTradeName: String = ""
    var branchCode: String = ""
    var country: String = ""
    var governate: String = ""
    var regionCity: String = ""
    var street: String = ""
    var buildingNumber: String = ""
    var postalCode: String = ""
    var floor: String = ""
    var room: String = ""
    var landmark: String = ""
    var additionalInformation: String = ""
    var deviceSerialNumber: String = ""
    var syndicateLicenseNumber: String = ""
    var activityCode: String = ""
    var posSerial: String = ""
    var posOsVersion: String = ""
    var clientId: String = ""
    var clientSecret: String = ""
}