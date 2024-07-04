package com.hris.modules

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import org.slf4j.LoggerFactory

fun Application.configureExceptionHandler() {

    val logger = LoggerFactory.getLogger(Application::class.java)
    install(StatusPages) {
        exception<BadRequestException> { call, exception ->
            logger.warn(exception.message, exception)
            call.respond(HttpStatusCode.BadRequest)
        }
        exception<ContentTransformationException> { call, exception ->
            logger.warn(exception.message, exception)
            call.respond(HttpStatusCode.BadRequest)
        }
    }
}
