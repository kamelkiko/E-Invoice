package di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import presentation.app.AppViewModel
import presentation.screen.history.HistoryViewModel
import presentation.screen.login.LoginViewModel
import presentation.screen.main.MainViewModel
import presentation.screen.receipts.ReceiptViewModel
import presentation.screen.search.SearchViewModel
import presentation.screen.settings.SettingsViewModel
import presentation.screen.setup.SetupViewModel

val viewModelModule = module {
    factoryOf(::AppViewModel)
    factoryOf(::LoginViewModel)
    factoryOf(::MainViewModel)
    factoryOf(::ReceiptViewModel)
    factoryOf(::SettingsViewModel)
    factoryOf(::HistoryViewModel)
    factoryOf(::SearchViewModel)
    factoryOf(::SetupViewModel)
}