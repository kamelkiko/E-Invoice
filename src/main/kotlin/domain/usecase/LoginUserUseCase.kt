package domain.usecase

import data.util.AppConstants
import domain.gateway.local.ILocalUserGateway

class LoginUserUseCase(
    private val loginLocalGateway: ILocalUserGateway,
    private val adminSystemUseCase: AdminSystemUseCase,
) {
    suspend fun loginUser(username: String, password: String, isKeepLogin: Boolean) {
        if (adminSystemUseCase.checkPermissionWithPassword(password)) {
            AppConstants.isAdmin = true
            return
        }
        loginLocalGateway.getUserByAuthenticate(username, password, isKeepLogin).also {
            AppConstants.isAdmin = false
        }
    }
}