package com.hris.review.di

import com.hris.review.config.Settings
import com.hris.review.repository.ReviewRepository
import com.hris.review.repository.ReviewRepositoryImpl
import com.hris.review.service.ReviewService
import com.hris.review.service.ReviewServiceImpl
import com.hris.review.util.Utils
import io.ktor.server.config.*
import org.jetbrains.exposed.sql.Database
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton


fun settingsModule(config: ApplicationConfig) = DI.Module("settingsModule") {
    bind<Settings>() with singleton { Settings(config) }
    bind<Utils>() with singleton { Utils(instance()) }
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

val reviewsModule = DI.Module("employeesModule") {
    bind<ReviewRepository>() with singleton { ReviewRepositoryImpl() }
    bind<ReviewService>() with singleton { ReviewServiceImpl(instance()) }
}
