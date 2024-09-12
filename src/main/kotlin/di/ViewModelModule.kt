package di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import presentation.app.AppViewModel
import presentation.screen.history.HistoryViewModel
import presentation.screen.login.LoginViewModel
import presentation.screen.main.MainViewModel
import presentation.screen.receipts.ReceiptViewModel
import presentation.screen.settings.SettingsViewModel

val viewModelModule = module {
    factoryOf(::AppViewModel)
    factoryOf(::LoginViewModel)
    factoryOf(::MainViewModel)
    factoryOf(::ReceiptViewModel)
    factoryOf(::SettingsViewModel)
    factoryOf(::HistoryViewModel)
}