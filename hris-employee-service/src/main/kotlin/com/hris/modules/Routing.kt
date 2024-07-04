package com.hris.modules

import com.hris.api.employeeRoute
import com.hris.api.hierarchyRoute
import io.github.smiley4.ktorswaggerui.SwaggerUI
import io.github.smiley4.ktorswaggerui.routing.openApiSpec
import io.github.smiley4.ktorswaggerui.routing.swaggerUI
import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.routing.*
import org.slf4j.event.Level

fun Application.configureRouting() {
    routing {
        route("api/v1") {
            employeeRoute()
            hierarchyRoute()
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
            description = "An api documentation for employee-service."
        }
    }
    install(CallLogging) {
        level = Level.INFO
    }
}
