package com.hris.modules

import com.hris.di.*
import io.ktor.server.application.*
import org.kodein.di.ktor.di

fun Application.configureDI() {
    di {
        import(settingsModule(environment.config))
        import(databaseModule)
        import(employeesModule)
        import(hierarchyModule)
        import(redisModule)
    }
}
