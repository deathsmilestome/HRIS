package com.hris.review.integration

import com.hris.review.Environment
import com.hris.review.dto.Review
import com.hris.review.testReview
import com.hris.review.testReviewId
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test


@Tag("ReviewApiTest")
class ReviewApiTest : Environment() {
    private val apiV = "api/v1"

    @Test
    fun reviewControllerTests() = testApplication {
        environment { config = ApplicationConfig("application-test.yaml") }
        // Get review by id
        // Status codes
        assertEquals(HttpStatusCode.BadRequest, client.get("$apiV/review/null").status)
        assertEquals(HttpStatusCode.OK, client.get("$apiV/review/$testReviewId").status)
        // Body
        val body1 = client.get("$apiV/review/$testReviewId").body<String>()
        assertEquals(testReview, Json.decodeFromString<Review>(body1))

        // Get review by employee id
        // Status codes
        assertEquals(HttpStatusCode.NoContent, client.get("$apiV/review/employee/0").status)
        assertEquals(HttpStatusCode.BadRequest, client.get("$apiV/review/employee/null").status)
        assertEquals(HttpStatusCode.OK, client.get("$apiV/review/employee/${testReview.employeeId}").status)
        // Body
        val body2 = client.get("$apiV/review/employee/${testReview.employeeId}").body<String>()
        assertEquals(testReview, Json.decodeFromString<List<Review>>(body2)[0])

        // Get review by employee id created before date
        // Status codes
        assertEquals(HttpStatusCode.BadRequest, client.get("$apiV/review/employee/null/before/01-01-2000").status)
        assertEquals(
            HttpStatusCode.BadRequest,
            client.get("$apiV/review/employee/${testReview.employeeId}/before/null").status
        )
        assertEquals(
            HttpStatusCode.NoContent,
            client.get("$apiV/review/employee/${testReview.employeeId}/before/01-01-2000").status
        )
        assertEquals(
            HttpStatusCode.OK,
            client.get("$apiV/review/employee/${testReview.employeeId}/before/01-01-2030").status
        )
        // Body
        val body3 = client.get("$apiV/review/employee/${testReview.employeeId}/before/01-01-2030").body<String>()
        assertEquals(testReview, Json.decodeFromString<List<Review>>(body3)[0])

        // Get review by employee id created after date
        // Status codes
        assertEquals(HttpStatusCode.BadRequest, client.get("$apiV/review/employee/null/after/01-01-3000").status)
        assertEquals(
            HttpStatusCode.BadRequest,
            client.get("$apiV/review/employee/${testReview.employeeId}/after/null").status
        )
        assertEquals(
            HttpStatusCode.NoContent,
            client.get("$apiV/review/employee/${testReview.employeeId}/after/01-01-3000").status
        )
        assertEquals(HttpStatusCode.OK, client.get("$apiV/review/employee/${testReview.employeeId}/after/01-01-2000").status)
        // Body
        val body4 = client.get("$apiV/review/employee/${testReview.employeeId}/after/01-01-2000").body<String>()
        assertEquals(testReview, Json.decodeFromString<List<Review>>(body4)[0])

        // Get review by employee id created between two dates
        // Status codes
        assertEquals(
            HttpStatusCode.BadRequest,
            client.get("$apiV/review/employee/null/between/01-01-2000/01-01-3000").status
        )
        assertEquals(
            HttpStatusCode.BadRequest,
            client.get("$apiV/review/employee/${testReview.employeeId}/between/null/01-01-3000").status
        )
        assertEquals(
            HttpStatusCode.BadRequest,
            client.get("$apiV/review/employee/${testReview.employeeId}/between/01-01-2000/null").status
        )
        assertEquals(
            HttpStatusCode.NoContent,
            client.get("$apiV/review/employee/${testReview.employeeId}/between/01-01-2000/01-01-2001").status
        )
        assertEquals(
            HttpStatusCode.OK,
            client.get("$apiV/review/employee/${testReview.employeeId}/between/01-01-2000/01-01-3000").status
        )
        // Body
        val body = client.get("$apiV/review/employee/${testReview.employeeId}/between/01-01-2000/01-01-3000").body<String>()
        assertEquals(testReview, Json.decodeFromString<List<Review>>(body)[0])

        // Create review
        // Status codes
        assertEquals(HttpStatusCode.OK, client.post("$apiV/review") {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            setBody(Json.encodeToString(Review.serializer(), testReview))
        }.status)
        assertEquals(HttpStatusCode.BadRequest, client.post("$apiV/review") {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            setBody("invalid body")
        }.status)

        // Update review
        // Status codes
        assertEquals(HttpStatusCode.OK, client.put("$apiV/review") {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            setBody(Json.encodeToString(Review.serializer(), testReview))
        }.status)
        assertEquals(HttpStatusCode.BadRequest, client.put("$apiV/review") {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            setBody("invalid body")
        }.status)

        // Delete review by id
        // Status codes
        assertEquals(HttpStatusCode.OK, client.delete("$apiV/review/$testReviewId").status)
        assertEquals(HttpStatusCode.BadRequest, client.delete("$apiV/review/null").status)
    }
}
