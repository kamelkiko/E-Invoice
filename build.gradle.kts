import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.kotlin.plugin.serialization") version ("2.0.0")
    id("io.realm.kotlin") version ("2.0.0")
}

group = "com.abapps"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
//    configurations.all {
//        resolutionStrategy {
//            force("com.guardsquare:proguard-gradle:7.5.0")
//            force("com.guardsquare:proguard-core:9.1.6")
//            force("com.guardsquare:proguard-base:7.5.0")
//        }
//    }
//    implementation("com.guardsquare:proguard-gradle:7.5.0")
//    implementation("com.guardsquare:proguard-core:9.1.6")
//    implementation("com.guardsquare:proguard-base:7.5.0")

    // Core
    implementation(compose.desktop.currentOs)
    implementation(compose.desktop.common)
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    implementation("com.google.code.gson:gson:2.11.0")
    // Compose
    implementation(compose.runtime)
    implementation(compose.foundation)
    implementation(compose.material)
    implementation(compose.material3)
    implementation(compose.materialIconsExtended)
    implementation(compose.ui)
    implementation(compose.components.resources)
    implementation(compose.components.uiToolingPreview)
    // ViewModel
    implementation("org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0")
    // Coroutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0-RC.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.9.0-RC.2")
    // Koin
    implementation("io.insert-koin:koin-core:3.5.6")
    implementation("io.insert-koin:koin-logger-slf4j:3.5.6")
    // Ktor
    implementation("io.ktor:ktor-client-core:2.3.12")
    implementation("io.ktor:ktor-client-okhttp:2.3.12")
    implementation("io.ktor:ktor-client-logging:2.3.12")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.12")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.12")
    // Realm DB
    implementation("io.realm.kotlin:library-base:2.0.0")
    // Voyager
    implementation("cafe.adriel.voyager:voyager-navigator:1.1.0-beta02")
    implementation("cafe.adriel.voyager:voyager-transitions:1.1.0-beta02")
    implementation("cafe.adriel.voyager:voyager-tab-navigator:1.1.0-beta02")
    implementation("cafe.adriel.voyager:voyager-screenmodel:1.1.0-beta02")
    implementation("cafe.adriel.voyager:voyager-koin:1.1.0-beta02")
    // Sql Driver
    implementation("com.microsoft.sqlserver:mssql-jdbc:9.4.1.jre8")
    implementation("com.vladsch.kotlin-jdbc:kotlin-jdbc:0.5.0")
    // Excel
    implementation("io.github.evanrupert:excelkt:1.0.2")
}

compose.desktop {
    application {
        mainClass = "MainKt"
        buildTypes.release.proguard {
            obfuscate.set(true)
        }
        nativeDistributions {
            targetFormats(TargetFormat.Exe)
            packageName = "E-Receipt"
            packageVersion = "1.0.0"
            windows {
                packageVersion = "1.0.0"
                msiPackageVersion = "1.0.0"
                exePackageVersion = "1.0.0"
                includeAllModules = true
                vendor = "AbApps"
                description = "E-Receipt App"
                copyright = "© 2024 AbApps. All rights reserved."
            }
        }
    }
}