package di

import data.local.model.AppConfigurationCollection
import data.local.model.UserCollection
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import org.koin.dsl.module

val localStorageModule = module {
    single {
        RealmConfiguration.Builder(
            schema = setOf(
                UserCollection::class,
                AppConfigurationCollection::class,
            )
        ).compactOnLaunch().deleteRealmIfMigrationNeeded().build()
    }
    single { Realm.open(configuration = get<RealmConfiguration>()) }
}