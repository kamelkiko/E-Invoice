package domain.util

open class EInvoiceException(override val message: String?) : Exception(message)

open class UnAuthException(override val message: String?) : EInvoiceException(message)

class InvalidCredentialsException(override val message: String) : UnAuthException(message)

class NetworkException(override val message: String?) : EInvoiceException(message)

class NotFoundException(override val message: String?) : EInvoiceException(message)

open class ValidationNetworkException(override val message: String?) : EInvoiceException(message)

class InvalidInvoiceException(override val message: String?) : ValidationNetworkException(message)

open class ValidationException(override val message: String?) : EInvoiceException(message)

class StartDateCannotBeBlank(override val message: String) : ValidationException(message)

class EndDateCannotBeBlank(override val message: String) : ValidationException(message)

class UUIDCannotBeBlank(override val message: String) : ValidationException(message)

class EmptyDataException(override val message: String?) : EInvoiceException(message)

class UnknownErrorException(override val message: String?) : EInvoiceException(message)

class ServerErrorException(override val message: String?) : EInvoiceException(message)