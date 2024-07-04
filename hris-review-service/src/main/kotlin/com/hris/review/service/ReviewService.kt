package com.hris.review.service

import com.hris.review.dto.Review
import java.time.LocalDateTime
import java.util.*

interface ReviewService {

    suspend fun getReview(id: UUID): Review?

    suspend fun getReviewsByEmployeeId(employeeId: Long): List<Review>?

    suspend fun getReviewsByEmployeeIdBefore(employeeId: Long, date: LocalDateTime): List<Review>?

    suspend fun getReviewsByEmployeeIdAfter(employeeId: Long, date: LocalDateTime): List<Review>?

    suspend fun getReviewsByEmployeeIdBetween(
        employeeId: Long,
        dateAfter: LocalDateTime,
        dateBefore: LocalDateTime,
    ): List<Review>?

    suspend fun createReview(review: Review): UUID

    suspend fun updateReview(review: Review)

    suspend fun deleteReview(id: UUID)
}
