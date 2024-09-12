package domain.gateway.local

import domain.entity.User

interface ILocalUserGateway {
    suspend fun getUser(): User
    suspend fun getUserByAuthenticate(userName: String, password: String, isKeepLogin: Boolean): User
    suspend fun addUser(user: User): Boolean
    suspend fun updateUser(username: String, password: String): Boolean
}