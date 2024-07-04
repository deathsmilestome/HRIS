package com.hris.review.modules

import com.hris.review.api.reviewRoute
import io.github.smiley4.ktorswaggerui.SwaggerUI
import io.github.smiley4.ktorswaggerui.routing.openApiSpec
import io.github.smiley4.ktorswaggerui.routing.swaggerUI
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        route("api/v1") {
            reviewRoute()
        }
        route("/swagger") {
            swaggerUI("/api.json")
        }
        route("/api.json") {
            openApiSpec()
        }
    }
    install(SwaggerUI) {
        info {
            title = "API"
            description = "An api documentation for review-service."
        }
    }
}
