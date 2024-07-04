package com.hris.di

import com.hris.cache.Cache
import com.hris.cache.EmployeeCache
import com.hris.config.Settings
import com.hris.dto.Employee
import com.hris.repository.EmployeeRepository
import com.hris.repository.EmployeeRepositoryImpl
import com.hris.service.EmployeeService
import com.hris.service.EmployeeServiceImpl
import com.hris.service.HierarchyService
import com.hris.service.HierarchyServiceImpl
import io.github.crackthecodeabhi.kreds.connection.Endpoint
import io.github.crackthecodeabhi.kreds.connection.KredsClient
import io.github.crackthecodeabhi.kreds.connection.newClient
import io.ktor.server.config.*
import org.jetbrains.exposed.sql.Database
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

fun settingsModule(config: ApplicationConfig) = DI.Module("settingsModule") {
    bind<Settings>() with singleton { Settings(config) }
}

val databaseModule = DI.Module("databaseModule") {
    bind<Database>() with singleton {
        val settings: Settings by di.instance()
        Database.connect(
            url = settings.db.dbUrl,
            user = settings.db.dbUser,
            driver = settings.db.dbDriver,
            password = settings.db.dbPassword
        )
    }
}

val employeesModule = DI.Module("employeesModule") {
    bind<EmployeeRepository>() with singleton { EmployeeRepositoryImpl() }
    bind<EmployeeService>() with singleton { EmployeeServiceImpl(instance(), instance()) }
}

val hierarchyModule = DI.Module("hierarchyModule") {
    bind<HierarchyService>() with singleton { HierarchyServiceImpl(instance()) }
}

val redisModule by DI.Module("redisModule") {
    bind<KredsClient>() with singleton {
        val settings: Settings by di.instance()
        newClient(
            Endpoint(
                host = settings.redis.redisHost,
                port = settings.redis.redisPort.toInt()
            )
        )
    }
    bind<Cache<Employee>>() with singleton {
        val settings: Settings by di.instance()
        EmployeeCache(instance(), settings.redis.ttl.toULong())
    }
}
