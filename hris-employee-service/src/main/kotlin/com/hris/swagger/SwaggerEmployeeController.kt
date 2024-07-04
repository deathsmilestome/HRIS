package com.hris.swagger

import com.hris.dto.Employee
import io.github.smiley4.ktorswaggerui.dsl.routes.OpenApiRoute
import io.ktor.http.*

fun OpenApiRoute.descriptionGetEmployeeById() {
    description = "Get employee by id"
    request {
        pathParameter<Long>("id") {
            description = "Employee Id"
        }
    }
    response {
        HttpStatusCode.OK to {
            body<Employee> {
                description = "Employee"
                example("Employee") {
                    value = Employee(
                        id = 2L,
                        name = "Tyler",
                        surname = "Durden",
                        mail = "tayler@project.debacle",
                        position = "none",
                        supervisorId = 1L,
                        createdAt = java.time.LocalDateTime.now()
                            .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.uuuuuu"))
                            .toString(),
                        lastModifiedAt = java.time.LocalDateTime.now()
                            .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.uuuuuu"))
                            .toString()
                    )
                }
            }
        }
        HttpStatusCode.BadRequest to {
            description = "Invalid id provided"
        }
    }
}

fun OpenApiRoute.descriptionCreateEmployee() {
    description = "Create new Employee"
    request {
        body<Employee> {
            description = "Employee without id and until dates. Supervisor id can be null"
            required = true
            example("Employee") {
                value = Employee(
                    name = "Tyler",
                    surname = "Durden",
                    mail = "tayler@project.debacle",
                    position = "none",
                    supervisorId = 1L,
                    numberOfSubordinates = 1
                )
            }
        }
    }
    response {
        HttpStatusCode.OK to {
            body<Long> {
                description = "Employee id"
                example("Long") {
                    value = 2L
                }
            }
        }
        HttpStatusCode.BadRequest to {
            description = "Invalid review body provided"
        }
    }
}

fun OpenApiRoute.descriptionUpdateEmployee() {
    description = "Update Employee"
    request {
        body<Employee> {
            description = "Employee id should be provided. Supervisor id can be null"
            required = true
            example("Employee") {
                value = Employee(
                    id = 1L,
                    name = "Tyler",
                    surname = "Durden",
                    mail = "tayler@project.debacle",
                    position = "none",
                    supervisorId = 1L
                )
            }
        }
    }
    response {
        HttpStatusCode.OK to {
            description = "If updated successfully"
        }
        HttpStatusCode.BadRequest to {
            description = "Invalid employee body provided"
        }
    }
}

fun OpenApiRoute.descriptionDeleteEmployee() {
    description = "Delete employee by id"
    request {
        pathParameter<Long>("id") {
            description = "Employee Id"
        }
    }
    response {
        HttpStatusCode.OK to {
            description = "If deleted successfully"
        }
        HttpStatusCode.BadRequest to {
            description = "Invalid id provided"
        }
    }
}
