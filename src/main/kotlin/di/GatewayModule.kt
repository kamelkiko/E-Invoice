package di

import data.gateway.AuthGateway
import data.gateway.fake.FakeLoginGateway
import data.gateway.fake.FakeLogoutGateway
import data.gateway.local.LocalConfigurationGateway
import data.gateway.local.LocalReceiptGateway
import data.gateway.local.LocalUserGateway
import data.gateway.local.StoresGateway
import data.gateway.remote.GetReceiptDetails
import data.gateway.remote.SendReceiptGateway
import domain.gateway.IAuthGateway
import domain.gateway.IReceiptGateway
import domain.gateway.IStoresGateway
import domain.gateway.local.ILocalConfigurationGateway
import domain.gateway.local.ILocalUserGateway
import domain.gateway.remote.IGetReceiptDetails
import domain.gateway.remote.ILoginGateway
import domain.gateway.remote.ILogoutGateway
import domain.gateway.remote.ISendReceiptGateway
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val gatewayModule = module {
    singleOf(::LocalConfigurationGateway) bind ILocalConfigurationGateway::class
    singleOf(::FakeLoginGateway) bind ILoginGateway::class
    singleOf(::FakeLogoutGateway) bind ILogoutGateway::class
    singleOf(::LocalReceiptGateway) bind IReceiptGateway::class
    singleOf(::LocalUserGateway) bind ILocalUserGateway::class
    singleOf(::AuthGateway) bind IAuthGateway::class
    singleOf(::StoresGateway) bind IStoresGateway::class
    singleOf(::SendReceiptGateway) bind ISendReceiptGateway::class
    singleOf(::GetReceiptDetails) bind IGetReceiptDetails::class
}