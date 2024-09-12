package data.local.model

import io.realm.kotlin.types.RealmObject

class AppConfigurationCollection : RealmObject {
    var id: Int = 0
    var isStayedLoggedIn: Boolean = false
    var token: String = ""
    var username: String = ""
}