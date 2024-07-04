package com.hris.review.modules

import com.hris.review.repository.entity.Reviews
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun Application.configureDatabases() {
    val database: Database by closestDI().instance()

    transaction(database) {
        SchemaUtils.create(Reviews)
    }
}
