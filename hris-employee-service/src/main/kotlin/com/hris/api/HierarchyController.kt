package com.hris.api

import com.hris.dto.HierarchyNode
import com.hris.service.HierarchyService
import com.hris.swagger.*
import io.github.smiley4.ktorswaggerui.dsl.routing.get
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun Route.hierarchyRoute() {
    val hierarchyService: HierarchyService by closestDI().instance()

    get("/hierarchy/{id}", { descriptionGetHierarchyById() } ) {
        val hierarchy = call.parameters["id"]?.toLongOrNull()
            ?.let {
                hierarchyService.getDefaultHierarchy(it)
            }
            ?: call.respond(HttpStatusCode.BadRequest)
        call.respond(hierarchy)
    }

    get("/hierarchy/equals/{id}", { descriptionGetEqualsById() } ) {
        val equals = call.parameters["id"]?.toLongOrNull()
            ?.let { hierarchyService.getEquals(it) ?: call.respond(HttpStatusCode.NoContent) }
            ?: call.respond(HttpStatusCode.BadRequest)
        call.respond(equals)
    }

    get("/hierarchy/subordinates/{id}", { descriptionGetSubordinatesById() } ) {
        val subordinates = call.parameters["id"]?.toLongOrNull()
            ?.let { hierarchyService.getSubs(it) ?: call.respond(HttpStatusCode.NoContent) }
            ?: call.respond(HttpStatusCode.BadRequest)
        call.respond(subordinates)
    }

    get("/hierarchy/supervisor/{id}", { descriptionGetSupervisorById() } ) {
        val supervisor = call.parameters["id"]?.toLongOrNull()
            ?.let { hierarchyService.getSupervisor(it) ?: call.respond(HttpStatusCode.NoContent) }
            ?: call.respond(HttpStatusCode.BadRequest)
        call.respond(supervisor)
    }

    get("/hierarchy/head/{id}", { descriptionGetHierarchyToHeadById() } ) {
        val hierarchy = call.parameters["id"]?.toLongOrNull()
            ?.let {
                hierarchyService.getAllToHead(it)
            }
            ?: call.respond(HttpStatusCode.BadRequest)
        call.respond(hierarchy)
    }
}
