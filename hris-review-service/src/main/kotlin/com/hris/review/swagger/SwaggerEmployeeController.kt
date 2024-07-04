package com.hris.review.swagger

import com.hris.review.dto.Review
import com.hris.review.util.Utils
import io.github.smiley4.ktorswaggerui.dsl.routes.OpenApiRoute
import io.ktor.http.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

fun OpenApiRoute.descriptionGetReviewById(utils: Utils) {
    description = "Get review by review id"
    request {
        pathParameter<String>("id") {
            description = "Review UUID"
        }
    }
    response {
        HttpStatusCode.OK to {
            body<Review> {
                description = "Review"
                example("Review") {
                    value = Review(
                        id = UUID.randomUUID().toString(),
                        employeeId = 1L,
                        aspirationForGrowth = 1,
                        performance = 1,
                        softSkills = 1,
                        independence = 1,
                        createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern(utils.dateFormat))
                    )
                }
            }
        }
        HttpStatusCode.BadRequest to {
            description = "Invalid id provided"
        }
    }
}

fun OpenApiRoute.descriptionGetReviewByEmployeeId(utils: Utils) {
    description = "Get reviews by employee id"
    request {
        pathParameter<Long>("employee_id") {
            description = "Employee id"
        }
    }
    response {
        HttpStatusCode.OK to {
            body<List<Review>> {
                description = "List of reviews for employee with provided id"
                example("Reviews") {
                    value = listOf(
                        Review(
                            id = UUID.randomUUID().toString(),
                            employeeId = 1L,
                            aspirationForGrowth = 1,
                            performance = 1,
                            softSkills = 1,
                            independence = 1,
                            createdAt = LocalDateTime.now()
                                .format(DateTimeFormatter.ofPattern(utils.dateFormat))
                        )
                    )
                }
            }
        }
        HttpStatusCode.NoContent to {
            description = "If no reviews found"
        }
        HttpStatusCode.BadRequest to {
            description = "Invalid id provided"
        }
    }
}

fun OpenApiRoute.descriptionGetReviewByEmployeeIdAndDateBefore(utils: Utils) {
    description = "Get reviews by employee id and created before provided date"
    request {
        pathParameter<Long>("employee_id") {
            description = "Employee id"
        }
        pathParameter<String>("date") {
            description = utils.dateFormatForReviewsFilter
        }
    }
    response {
        HttpStatusCode.OK to {
            body<List<Review>> {
                description =
                    "List of reviews for employee with provided id which were created before provided date"
                example("Reviews") {
                    value = listOf(
                        Review(
                            id = UUID.randomUUID().toString(),
                            employeeId = 1L,
                            aspirationForGrowth = 1,
                            performance = 1,
                            softSkills = 1,
                            independence = 1,
                            createdAt = LocalDateTime.now()
                                .format(DateTimeFormatter.ofPattern(utils.dateFormat))
                        )
                    )
                }
            }
        }
        HttpStatusCode.NoContent to {
            description = "If no reviews found"
        }
        HttpStatusCode.BadRequest to {
            description = "Invalid id provided"
        }
    }
}

fun OpenApiRoute.descriptionGetReviewByEmployeeIdAndDateAfter(utils: Utils) {
    description = "Get reviews by employee id and created after provided date"
    request {
        pathParameter<Long>("employee_id") {
            description = "Employee id"
        }
        pathParameter<String>("date") {
            description = utils.dateFormatForReviewsFilter
        }
    }
    response {
        HttpStatusCode.OK to {
            body<List<Review>> {
                description =
                    "List of reviews for employee with provided id which were created after provided date"
                example("Reviews") {
                    value = listOf(
                        Review(
                            id = UUID.randomUUID().toString(),
                            employeeId = 1L,
                            aspirationForGrowth = 1,
                            performance = 1,
                            softSkills = 1,
                            independence = 1,
                            createdAt = LocalDateTime.now()
                                .format(DateTimeFormatter.ofPattern(utils.dateFormat))
                        )
                    )
                }
            }
        }
        HttpStatusCode.NoContent to {
            description = "If no reviews found"
        }
        HttpStatusCode.BadRequest to {
            description = "Invalid id provided"
        }
    }
}

fun OpenApiRoute.descriptionGetReviewByEmployeeIdAndDateBetween(utils: Utils) {
    description = "Get reviews by employee id and created betwenn provided dates"
    request {
        pathParameter<Long>("employee_id") {
            description = "Employee id"
        }
        pathParameter<String>("date_after") {
            description = utils.dateFormatForReviewsFilter
        }
        pathParameter<String>("date_before") {
            description = utils.dateFormatForReviewsFilter
        }
    }
    response {
        HttpStatusCode.OK to {
            body<List<Review>> {
                description =
                    "List of reviews for employee with provided id which were created between provided dates"
                example("Reviews") {
                    value = listOf(
                        Review(
                            id = UUID.randomUUID().toString(),
                            employeeId = 1L,
                            aspirationForGrowth = 1,
                            performance = 1,
                            softSkills = 1,
                            independence = 1,
                            createdAt = LocalDateTime.now()
                                .format(DateTimeFormatter.ofPattern(utils.dateFormat))
                        )
                    )
                }
            }
        }
        HttpStatusCode.NoContent to {
            description = "If no reviews found"
        }
        HttpStatusCode.BadRequest to {
            description = "Invalid id provided"
        }
    }
}

fun OpenApiRoute.descriptionCreateReview() {
    description = "Create new Employee"
    request {
        body<Review> {
            description = "Review without id and until dates. Supervisor id can be null"
            required = true
            example("Review") {
                value = Review(
                    employeeId = 1L,
                    aspirationForGrowth = 1,
                    performance = 1,
                    softSkills = 1,
                    independence = 1,
                )
            }
        }

    }
    response {
        HttpStatusCode.OK to {
            body<String> {
                description = "Review UUID"
                example("UUID") {
                    value = UUID.randomUUID()
                }
            }
        }
        HttpStatusCode.BadRequest to {
            description = "Invalid review body provided"
        }
    }
}

fun OpenApiRoute.descriptionUpdateReview() {
    description = "Update review"
    request {
        body<Review> {
            description = "Review id should be provided."
            required = true
            example("Review") {
                value = Review(
                    id = UUID.randomUUID().toString(),
                    employeeId = 1L,
                    aspirationForGrowth = 1,
                    performance = 1,
                    softSkills = 1,
                    independence = 1,
                )
            }
        }
    }
    response {
        HttpStatusCode.OK to {
            description = "If updated successfully"
        }
        HttpStatusCode.BadRequest to {
            description = "Invalid review body provided"
        }
    }
}

fun OpenApiRoute.descriptionDeleteReview() {
    description = "Delete review by id"
    request {
        pathParameter<String>("id") {
            description = "Review UUID"
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
