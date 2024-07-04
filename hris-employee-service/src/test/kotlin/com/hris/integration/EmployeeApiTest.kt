package com.hris.integration

import com.hris.Environment
import com.hris.dto.Employee
import com.hris.testEmployee
import com.hris.testEmployeeId
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test


@Tag("EmployeeApiTest")
class EmployeeApiTest : Environment() {

    private val apiV = "api/v1"

    @Test
    fun employeeControllerTests() = testApplication {

        environment { config = ApplicationConfig("application-test.yaml") }

        // Get employee by id
        // Status codes
        assertEquals(HttpStatusCode.BadRequest, client.get("$apiV/employee/null").status)
        assertEquals(HttpStatusCode.OK, client.get("$apiV/employee/$testEmployeeId").status)
        // Body
        val body1 = client.get("$apiV/employee/$testEmployeeId").body<String>()
        assertEquals(testEmployee, Json.decodeFromString<Employee>(body1))

        // Create employee
        // Status codes
        assertEquals(HttpStatusCode.BadRequest, client.post("$apiV/employee") {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            setBody("invalid body")
        }.status)

        assertEquals(HttpStatusCode.OK, client.post("$apiV/employee") {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            setBody(Json.encodeToString(Employee.serializer(), testEmployee))
        }.status)

        // Update employee
        // Status codes
        assertEquals(HttpStatusCode.OK, client.put("$apiV/employee") {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            setBody(Json.encodeToString(Employee.serializer(), testEmployee))
        }.status)
        assertEquals(HttpStatusCode.BadRequest, client.put("$apiV/employee") {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            setBody("invalid body")
        }.status)

        // Delete employee by id
        // Status codes
        assertEquals(HttpStatusCode.OK, client.delete("$apiV/employee/$testEmployeeId").status)
        assertEquals(HttpStatusCode.BadRequest, client.delete("$apiV/employee/null").status)
    }
}
