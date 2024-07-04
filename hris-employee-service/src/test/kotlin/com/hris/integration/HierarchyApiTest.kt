package com.hris.integration

import com.hris.*
import com.hris.dto.HierarchyNode
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("HierarchyApiTest")
class HierarchyApiTest : Environment() {
    private val apiV = "api/v1"

    @Test
    fun hierarchyControllerTests() = testApplication {

        environment { config = ApplicationConfig("application-test.yaml") }

        // Get default hierarchy by id
        // Status codes
        assertEquals(HttpStatusCode.BadRequest, client.get("$apiV/hierarchy/null").status)
        assertEquals(HttpStatusCode.OK, client.get("$apiV/hierarchy/$testEmployeeId").status)
        // Body
        val hierarchy = client.get("$apiV/hierarchy/$testEmployeeId").body<String>()
        assertEquals(defaultHierarchyNodeForEmployee, Json.decodeFromString<HierarchyNode>(hierarchy))

        // Get equals by id
        // Status codes
        assertEquals(HttpStatusCode.BadRequest, client.get("$apiV/hierarchy/equals/null").status)
        assertEquals(HttpStatusCode.NoContent, client.get("$apiV/hierarchy/equals/$testEmployeeSupId").status)
        assertEquals(HttpStatusCode.OK, client.get("$apiV/hierarchy/equals/$testEmployeeFirstSubId").status)
        // Body
        // Get equals by id when employee actually have equals
        val equals = client.get("$apiV/hierarchy/equals/$testEmployeeSecondSubId").body<String>()
        assertEquals(
            listOf(
                HierarchyNode(
                    testEmployeeFirstSub.id.toString(),
                    testEmployeeFirstSub.name,
                    testEmployeeFirstSub.surname,
                    testEmployeeFirstSub.position
                )
            ), Json.decodeFromString<List<HierarchyNode>>(equals)
        )

        // Get subordinates by id
        // Status codes
        assertEquals(HttpStatusCode.BadRequest, client.get("$apiV/hierarchy/subordinates/null").status)
        assertEquals(
            HttpStatusCode.NoContent,
            client.get("$apiV/hierarchy/subordinates/$testEmployeeSecondSubId").status
        )
        assertEquals(HttpStatusCode.OK, client.get("$apiV/hierarchy/subordinates/$testEmployeeId").status)
        // Body
        // Get equals by id when employee actually have subordinates
        val subordinates = client.get("$apiV/hierarchy/subordinates/$testEmployeeSupId").body<String>()
        assertEquals(
            listOf(
                HierarchyNode(
                    testEmployee.id.toString(),
                    testEmployee.name,
                    testEmployee.surname,
                    testEmployee.position
                )
            ), Json.decodeFromString<List<HierarchyNode>>(subordinates)
        )

        // Get supervisor by id
        // Status codes
        assertEquals(HttpStatusCode.BadRequest, client.get("$apiV/hierarchy/supervisor/null").status)
        assertEquals(
            HttpStatusCode.NoContent,
            client.get("$apiV/hierarchy/supervisor/$testEmployeeSupId").status
        )
        assertEquals(HttpStatusCode.OK, client.get("$apiV/hierarchy/supervisor/$testEmployeeId").status)
        // Body
        // Get equals by id when employee actually have supervisor
        val supervisor = client.get("$apiV/hierarchy/supervisor/$testEmployeeId").body<String>()
        assertEquals(
            HierarchyNode(
                testEmployeeSup.id.toString(),
                testEmployeeSup.name,
                testEmployeeSup.surname,
                testEmployeeSup.position
            ), Json.decodeFromString<HierarchyNode>(supervisor)
        )

        // Get hierarchy from employee to head
        // Status codes
        assertEquals(HttpStatusCode.BadRequest, client.get("$apiV/hierarchy/head/null").status)
        assertEquals(HttpStatusCode.BadRequest, client.get("$apiV/hierarchy/head/666").status)

        assertEquals(HttpStatusCode.OK, client.get("$apiV/hierarchy/head/$testEmployeeId").status)
        // Body
        val hierarchyToHead = client.get("$apiV/hierarchy/head/$testEmployeeFirstSubId").body<String>()
        assertEquals(hierarchyNodeForHeadEndpoint, Json.decodeFromString<HierarchyNode>(hierarchyToHead))
        val hierarchyToHeadFromHead = client.get("$apiV/hierarchy/head/$testEmployeeSupId").body<String>()
        assertEquals(
            HierarchyNode(
                testEmployeeSupId.toString(),
                testEmployeeSup.name,
                testEmployeeSup.surname,
                testEmployeeSup.position
            ), Json.decodeFromString<HierarchyNode>(hierarchyToHeadFromHead)
        )
    }
}
