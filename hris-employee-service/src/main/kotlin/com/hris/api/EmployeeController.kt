package com.hris.api

import com.hris.dto.Employee
import com.hris.service.EmployeeService
import com.hris.swagger.descriptionCreateEmployee
import com.hris.swagger.descriptionDeleteEmployee
import com.hris.swagger.descriptionGetEmployeeById
import com.hris.swagger.descriptionUpdateEmployee
import io.github.smiley4.ktorswaggerui.dsl.routing.delete
import io.github.smiley4.ktorswaggerui.dsl.routing.get
import io.github.smiley4.ktorswaggerui.dsl.routing.post
import io.github.smiley4.ktorswaggerui.dsl.routing.put
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun Route.employeeRoute() {
    val employeeService: EmployeeService by closestDI().instance()

    get("/employee/{id}", { descriptionGetEmployeeById() } ) {
        val employee = call.parameters["id"]?.toLongOrNull()
            ?.let { employeeService.getEmployee(it) }
            ?: call.respond(HttpStatusCode.BadRequest)
        call.respond(employee)
    }

    post("/employee", { descriptionCreateEmployee() } ) {
        call.respond(employeeService.createEmployee(call.receive<Employee>()))
    }

    put("/employee", { descriptionUpdateEmployee() } ) {
        employeeService.updateEmployee(call.receive<Employee>())
        call.respond(HttpStatusCode.OK)
    }

    delete("/employee/{id}", { descriptionDeleteEmployee() } ) {
        call.parameters["id"]?.toLongOrNull()
            ?.let {
                employeeService.delete(it)
                call.respond(HttpStatusCode.OK)
            }
        call.respond(HttpStatusCode.BadRequest)
    }
}
