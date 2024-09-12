package presentation.base

sealed interface ErrorState {
    data class UnAuthorized(val message: String?) : ErrorState
    data class NetworkError(val message: String?) : ErrorState
    data class NotFound(val message: String?) : ErrorState
    data class ValidationNetworkError(val message: String?) : ErrorState
    data class ValidationError(val message: String?) : ErrorState
    data class EmptyData(val message: String?) : ErrorState
    data class UnknownError(val message: String?) : ErrorState
    data class ServerError(val message: String?) : ErrorState
    data class StartDateCannotBeBlank(val message: String?) : ErrorState
    data class EndDateCannotBeBlank(val message: String?) : ErrorState
    data class UUIDCannotBeBlank(val message: String?) : ErrorState
    data class InvalidCredentialsError(val message: String?) : ErrorState
    data class InvalidInvoiceError(val message: String?) : ErrorState
}