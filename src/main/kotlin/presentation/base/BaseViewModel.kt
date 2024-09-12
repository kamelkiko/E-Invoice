package presentation.base

import cafe.adriel.voyager.core.model.ScreenModel
import domain.util.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.datetime.Clock
import org.koin.core.component.KoinComponent

abstract class BaseViewModel<S, E>(initialState: S) : ScreenModel, KoinComponent {

    abstract val viewModelScope: CoroutineScope
    private val _state = MutableStateFlow(initialState)
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<E?>()
    val effect = _effect.asSharedFlow().throttleFirst(1000).mapNotNull { it }

    protected fun <T> tryToExecute(
        function: suspend () -> T,
        onSuccess: (T) -> Unit,
        onError: (ErrorState) -> Unit,
        inScope: CoroutineScope = viewModelScope,
    ): Job {
        return runWithErrorCheck(onError, inScope) {
            val result = function()
            onSuccess(result)
        }
    }

    protected suspend fun <T> tryToExecuteWithoutScope(
        function: suspend () -> T,
        onSuccess: (T) -> Unit,
        onError: (ErrorState) -> Unit,
    ) {
        try {
            val result = function()
            onSuccess(result)
        } catch (exception: UnAuthException) {
            onError(ErrorState.UnAuthorized(exception.message))
        } catch (exception: NotFoundException) {
            onError(ErrorState.NotFound(exception.message))
        } catch (exception: EmptyDataException) {
            onError(ErrorState.EmptyData(exception.message))
        } catch (exception: InvalidCredentialsException) {
            onError(ErrorState.InvalidCredentialsError(exception.message))
        } catch (exception: InvalidInvoiceException) {
            onError(ErrorState.InvalidInvoiceError(exception.message))
        } catch (exception: StartDateCannotBeBlank) {
            onError(ErrorState.StartDateCannotBeBlank(exception.message))
        } catch (exception: EndDateCannotBeBlank) {
            onError(ErrorState.EndDateCannotBeBlank(exception.message))
        } catch (exception: UUIDCannotBeBlank) {
            onError(ErrorState.UUIDCannotBeBlank(exception.message))
        } catch (exception: ValidationNetworkException) {
            onError(ErrorState.ValidationNetworkError(exception.message))
        } catch (exception: ValidationException) {
            onError(ErrorState.ValidationError(exception.message))
        } catch (exception: ServerErrorException) {
            onError(ErrorState.ServerError(exception.message))
        } catch (exception: NetworkException) {
            onError(ErrorState.NetworkError(exception.message))
        } catch (exception: UnknownErrorException) {
            onError(ErrorState.UnknownError(exception.message))
        } catch (exception: Exception) {
            onError(ErrorState.UnknownError(exception.message))
        }
    }

    protected fun <T> tryToCollect(
        function: suspend () -> Flow<T>,
        onNewValue: (T) -> Unit,
        onError: (ErrorState) -> Unit,
        inScope: CoroutineScope = viewModelScope,
    ): Job {
        return runWithErrorCheck(onError, inScope) {
            function().distinctUntilChanged().collectLatest {
                onNewValue(it)
            }
        }
    }

    protected fun updateState(updater: (S) -> S) {
        _state.update(updater)
    }

    protected fun sendNewEffect(newEffect: E) {
        viewModelScope.launch(Dispatchers.IO) {
            _effect.emit(newEffect)
        }
    }

    private fun runWithErrorCheck(
        onError: (ErrorState) -> Unit,
        inScope: CoroutineScope = viewModelScope,
        dispatcher: CoroutineDispatcher = Dispatchers.Default,
        function: suspend () -> Unit,
    ): Job {
        return inScope.launch(dispatcher) {
            try {
                function()
            } catch (exception: UnAuthException) {
                onError(ErrorState.UnAuthorized(exception.message))
            } catch (exception: NotFoundException) {
                onError(ErrorState.NotFound(exception.message))
            } catch (exception: EmptyDataException) {
                onError(ErrorState.EmptyData(exception.message))
            } catch (exception: InvalidCredentialsException) {
                onError(ErrorState.InvalidCredentialsError(exception.message))
            } catch (exception: InvalidInvoiceException) {
                onError(ErrorState.InvalidInvoiceError(exception.message))
            } catch (exception: StartDateCannotBeBlank) {
                onError(ErrorState.StartDateCannotBeBlank(exception.message))
            } catch (exception: EndDateCannotBeBlank) {
                onError(ErrorState.EndDateCannotBeBlank(exception.message))
            } catch (exception: UUIDCannotBeBlank) {
                onError(ErrorState.UUIDCannotBeBlank(exception.message))
            } catch (exception: ValidationNetworkException) {
                onError(ErrorState.ValidationNetworkError(exception.message))
            } catch (exception: ValidationException) {
                onError(ErrorState.ValidationError(exception.message))
            } catch (exception: ServerErrorException) {
                onError(ErrorState.ServerError(exception.message))
            } catch (exception: NetworkException) {
                onError(ErrorState.NetworkError(exception.message))
            } catch (exception: UnknownErrorException) {
                onError(ErrorState.UnknownError(exception.message))
            } catch (exception: Exception) {
                onError(ErrorState.UnknownError(exception.message))
            }
        }
    }

    private fun <T> Flow<T>.throttleFirst(periodMillis: Long): Flow<T> {
        require(periodMillis > 0)
        return flow {
            var lastTime = 0L
            collect { value ->
                val currentTime = Clock.System.now().toEpochMilliseconds()
                if (currentTime - lastTime >= periodMillis) {
                    lastTime = currentTime
                    emit(value)
                }
            }
        }
    }

    protected fun launchDelayed(duration: Long, block: suspend CoroutineScope.() -> Unit): Job {
        return viewModelScope.launch(Dispatchers.IO) {
            delay(duration)
            block()
        }
    }
}