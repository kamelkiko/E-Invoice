package data.local.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class UserCollection : RealmObject {
    @PrimaryKey
    var id: Int = 0
    var userName: String = ""
    var password: String = ""
}