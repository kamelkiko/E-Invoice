package di

import com.vladsch.kotlin.jdbc.HikariCP
import com.vladsch.kotlin.jdbc.SessionImpl
import org.koin.dsl.module
import java.io.File

val sqlDriverModule = module(createdAtStart = true) {
    single(createdAtStart = true) {
        val file = File("MyConn.txt")
        HikariCP.default(file.readLines()[0], file.readLines()[1], file.readLines()[2])
    }

    single(createdAtStart = true) {
        SessionImpl.defaultDataSource = { HikariCP.dataSource() }
    }
}