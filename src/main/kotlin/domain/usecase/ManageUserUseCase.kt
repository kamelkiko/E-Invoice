package domain.usecase

import domain.entity.User
import domain.gateway.local.ILocalConfigurationGateway
import domain.gateway.local.ILocalUserGateway

class ManageUserUseCase(
    private val localConfigurationGateway: ILocalConfigurationGateway,
    private val localUserGateway: ILocalUserGateway
) {
    suspend fun getUserName(): String {
        return localConfigurationGateway.getUserName()
    }

    suspend fun createUser(user: User, id: Int): Boolean {
        return localUserGateway.addUser(user).also { isSuccess ->
            if (isSuccess)
                localConfigurationGateway.saveUserName(user.name)
        }
    }

    suspend fun getUser(): User {
        return localUserGateway.getUser()
    }

    suspend fun updateUser(username: String, password: String, id: Int): Boolean {
        return localUserGateway.updateUser(username, password).also { isSuccess ->
            if (isSuccess)
                localConfigurationGateway.saveUserName(username)
        }
    }
}