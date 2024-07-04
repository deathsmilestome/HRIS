package com.hris.review.modules

import com.hris.review.di.databaseModule
import com.hris.review.di.reviewsModule
import com.hris.review.di.settingsModule
import io.ktor.server.application.*
import org.kodein.di.ktor.di

fun Application.configureDI() {
    di {
        import(settingsModule(environment.config))
        import(databaseModule)
        import(reviewsModule)
    }
}
