package com.hris.review.service

import com.hris.review.dto.Review
import java.time.LocalDateTime
import java.util.*

class ReviewServiceImpl(
    private val reviewRepository: com.hris.review.repository.ReviewRepository,
) : ReviewService {
    override suspend fun getReview(id: UUID): Review? {
        return reviewRepository.read(id)
    }

    override suspend fun getReviewsByEmployeeId(employeeId: Long): List<Review>? {
        return reviewRepository.getReviewsByEmployeeId(employeeId).ifEmpty { null }
    }

    override suspend fun getReviewsByEmployeeIdBefore(employeeId: Long, date: LocalDateTime): List<Review>? {
        return reviewRepository.getReviewsByEmployeeIdBefore(employeeId, date).ifEmpty { null }
    }

    override suspend fun getReviewsByEmployeeIdAfter(employeeId: Long, date: LocalDateTime): List<Review>? {
        return reviewRepository.getReviewsByEmployeeIdAfter(employeeId, date).ifEmpty { null }
    }

    override suspend fun getReviewsByEmployeeIdBetween(
        employeeId: Long,
        dateAfter: LocalDateTime,
        dateBefore: LocalDateTime,
    ): List<Review>? {
        return reviewRepository.getReviewsByEmployeeIdBetween(employeeId, dateAfter, dateBefore).ifEmpty { null }
    }

    override suspend fun createReview(review: Review): UUID {
        return reviewRepository.create(review)
    }

    override suspend fun updateReview(review: Review) {
        UUID.fromString(review.id).let {
            reviewRepository.update(it, review)
        }
    }

    override suspend fun deleteReview(id: UUID) {
        reviewRepository.delete(id)
    }
}
