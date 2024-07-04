package com.hris.review.api


import com.hris.review.dto.Review
import com.hris.review.swagger.*
import com.hris.review.util.Utils
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
import java.util.*

fun Route.reviewRoute() {
    val reviewService: com.hris.review.service.ReviewService by closestDI().instance()
    val utils: Utils by closestDI().instance()

    get("/review/{id}", { descriptionGetReviewById(utils) }) {
        val review = call.parameters["id"]
            ?.let { reviewService.getReview(UUID.fromString(it)) }
            ?: call.respond(HttpStatusCode.BadRequest)
        call.respond(review)
    }

    get("/review/employee/{employee_id}", { descriptionGetReviewByEmployeeId(utils) }) {
        val review = call.parameters["employee_id"]?.toLongOrNull()
            ?.let { reviewService.getReviewsByEmployeeId(it) ?: call.respond(HttpStatusCode.NoContent) }
            ?: call.respond(HttpStatusCode.BadRequest)
        call.respond(review)
    }

    get("/review/employee/{employee_id}/before/{date}", { descriptionGetReviewByEmployeeIdAndDateBefore(utils) }) {
        val date = utils.getDateFromString(call.parameters["date"])
        val review = call.parameters["employee_id"]?.toLongOrNull()
            ?.let {
                reviewService.getReviewsByEmployeeIdBefore(it, date)
                    ?: call.respond(HttpStatusCode.NoContent)
            }
            ?: call.respond(HttpStatusCode.BadRequest)
        call.respond(review)
    }

    get("/review/employee/{employee_id}/after/{date}", { descriptionGetReviewByEmployeeIdAndDateAfter(utils) }) {
        val date = utils.getDateFromString(call.parameters["date"])
        val review = call.parameters["employee_id"]?.toLongOrNull()
            ?.let {
                reviewService.getReviewsByEmployeeIdAfter(it, date)
                    ?: call.respond(HttpStatusCode.NoContent)
            }
            ?: call.respond(HttpStatusCode.BadRequest)
        call.respond(review)
    }

    get(
        "/review/employee/{employee_id}/between/{date_after}/{date_before}",
        { descriptionGetReviewByEmployeeIdAndDateBetween(utils) }) {
        val dateBefore = utils.getDateFromString(call.parameters["date_before"])
        val dateAfter = utils.getDateFromString(call.parameters["date_after"])
        val review = call.parameters["employee_id"]?.toLongOrNull()
            ?.let {
                reviewService.getReviewsByEmployeeIdBetween(it, dateAfter, dateBefore)
                    ?: call.respond(HttpStatusCode.NoContent)
            }
            ?: call.respond(HttpStatusCode.BadRequest)
        call.respond(review)
    }

    post("/review", { descriptionCreateReview() }) {
        call.respond(reviewService.createReview(call.receive<Review>()).toString())

    }

    put("/review", { descriptionUpdateReview() }) {
        reviewService.updateReview(call.receive<Review>())
        call.respond(HttpStatusCode.OK)

    }

    delete("/review/{id}", { descriptionDeleteReview() }) {
        UUID.fromString(call.parameters["id"]).let {
            reviewService.deleteReview(it)
            call.respond(HttpStatusCode.OK)
        }
    }
}
