package com.hris.review

import com.hris.review.dto.Review
import com.hris.review.repository.ReviewRepository
import io.ktor.server.application.*
import kotlinx.coroutines.runBlocking
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import java.util.*


lateinit var testReviewId: UUID
var testReview= Review(null, 1, 1, 1, 1, 1)


fun Application.initTestData() {


    val db: ReviewRepository by closestDI().instance()

    runBlocking {
        testReviewId = db.create(testReview)
        testReview = db.read(testReviewId) ?: throw Exception("No testReview in database in testcontainer")
    }
}