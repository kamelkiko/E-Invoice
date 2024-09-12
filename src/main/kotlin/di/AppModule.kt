package di

import org.koin.dsl.module

val AppModule = module {
    includes(useCaseModule, networkModule, localStorageModule, gatewayModule, viewModelModule, sqlDriverModule)
}