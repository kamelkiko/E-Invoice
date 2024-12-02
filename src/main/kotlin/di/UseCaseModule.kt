package di

import domain.usecase.*
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val useCaseModule = module {
    factoryOf(::LoginUserUseCase)
    factoryOf(::LogoutUserUseCase)
    factoryOf(::ManageReceiptUseCase)
    factoryOf(::AdminSystemUseCase)
    factoryOf(::ManageUserUseCase)
    factoryOf(::ManageSetupStoreUseCase)
    factoryOf(::CreateTokenUseCase)
    factoryOf(::GetIsLoggedInUseCase)
    factoryOf(::GetAllStoresUseCase)
    factoryOf(::SendReceiptUseCase)
    factoryOf(::GetReceiptDetailsUseCase)
}