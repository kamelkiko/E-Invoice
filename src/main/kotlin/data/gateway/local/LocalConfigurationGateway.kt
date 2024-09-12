package data.gateway.local

import data.local.model.AppConfigurationCollection
import data.util.AppLanguage
import domain.gateway.local.ILocalConfigurationGateway
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.first

class LocalConfigurationGateway(private val realm: Realm) : ILocalConfigurationGateway {

    init {
        createUserConfiguration()
    }

    private fun createUserConfiguration() {
        realm.writeBlocking {
            copyToRealm(AppConfigurationCollection().apply {
                id = CONFIGURATION_ID
            }
            )
        }
    }

    override suspend fun getLanguageCode(): String {
        return AppLanguage.code.first()
    }

    override suspend fun getIsStayedLoggedIn(): Boolean {
        return realm.query(
            AppConfigurationCollection::class,
            "$ID == $CONFIGURATION_ID"
        ).first().find()?.isStayedLoggedIn ?: false
    }

    override suspend fun getToken(): String {
        return realm.query(
            AppConfigurationCollection::class,
            "$ID == $CONFIGURATION_ID"
        ).first().find()?.token ?: ""
    }

    override suspend fun getUserName(): String {
        return realm.query<AppConfigurationCollection>("$ID == $CONFIGURATION_ID").first()
            .find()?.username ?: ""
    }

    override suspend fun clearConfiguration() {
        realm.write { delete(query<AppConfigurationCollection>()) }
    }

    override suspend fun saveToken(token: String) {
        realm.write {
            query(AppConfigurationCollection::class, "$ID == $CONFIGURATION_ID").first()
                .find()?.token = token
        }
    }

    override suspend fun saveUserName(username: String) {
        realm.write {
            query<AppConfigurationCollection>("$ID == $CONFIGURATION_ID").first().find()?.username =
                username
        }
    }

    override suspend fun saveIsStayedLoggedIn(isStayedLoggedIn: Boolean) {
        realm.write {
            query(AppConfigurationCollection::class, "$ID == $CONFIGURATION_ID").first()
                .find()?.isStayedLoggedIn = isStayedLoggedIn
        }
    }

    companion object {
        private const val CONFIGURATION_ID = 0
        private const val ID = "id"
    }
}