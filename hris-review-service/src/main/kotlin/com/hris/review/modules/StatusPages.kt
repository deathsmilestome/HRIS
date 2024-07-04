package com.hris.review.modules

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import org.slf4j.LoggerFactory
import java.time.format.DateTimeParseException

fun Application.configureExceptionHandler() {

    val logger = LoggerFactory.getLogger(Application::class.java)
    install(StatusPages) {
        exception<DateTimeParseException> { call, exception ->
            call.respond(HttpStatusCode.BadRequest)
            logger.warn(exception.message, exception)
        }

        exception<ContentTransformationException> { call, exception ->
            call.respond(HttpStatusCode.BadRequest)
            logger.warn(exception.message, exception)
        }

        exception<IllegalArgumentException> { call, exception ->
            call.respond(HttpStatusCode.BadRequest)
            logger.warn(exception.message, exception)
        }
    }
}
