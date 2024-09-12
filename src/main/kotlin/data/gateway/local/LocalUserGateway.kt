package data.gateway.local

import data.local.mapper.toUser
import data.local.mapper.toUserCollection
import data.local.model.AppConfigurationCollection
import data.local.model.UserCollection
import domain.entity.User
import domain.gateway.local.ILocalUserGateway
import domain.util.NotFoundException
import domain.util.UnAuthException
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query

class LocalUserGateway(private val realm: Realm) : ILocalUserGateway {

    override suspend fun addUser(user: User): Boolean {
        realm.write {
            copyToRealm(user.toUserCollection(), updatePolicy = UpdatePolicy.ALL)
        }
        return true
    }

    override suspend fun updateUser(username: String, password: String): Boolean {
        realm.query<UserCollection>("$ID == $CONFIGURATION_ID").first().find()?.apply {
            this.userName = username
            this.password = password
        }
        return true
    }

    override suspend fun getUser(): User {
        return realm.query<UserCollection>().first().find()?.toUser()
            ?: throw NotFoundException("Not Found")
    }

    override suspend fun getUserByAuthenticate(userName: String, password: String, isKeepLogin: Boolean): User {
        return realm.query<UserCollection>("$USER_NAME == '$userName' AND $PASSWORD == '$password'")
            .first().find()?.toUser().also {
                realm.write {
                    query(AppConfigurationCollection::class, "$ID == $CONFIGURATION_ID").first()
                        .find()?.isStayedLoggedIn = isKeepLogin
                }
            } ?: throw UnAuthException("Invalid user")
    }

    private companion object {
        private const val USER_NAME = "userName"
        private const val PASSWORD = "password"
        private const val CONFIGURATION_ID = 0
        private const val ID = "id"
    }

}
