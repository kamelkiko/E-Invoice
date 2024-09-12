package domain.gateway.local

interface ILocalConfigurationGateway {
    suspend fun getLanguageCode(): String
    suspend fun getIsStayedLoggedIn(): Boolean
    suspend fun getToken(): String
    suspend fun clearConfiguration()

    suspend fun saveToken(token: String)
    suspend fun saveIsStayedLoggedIn(isStayedLoggedIn: Boolean)
    // suspend fun getThemeMode(): Flow<Boolean>
   // suspend fun updateThemeMode(mode: Boolean)
    suspend fun getUserName(): String
    suspend fun saveUserName(username: String)
}