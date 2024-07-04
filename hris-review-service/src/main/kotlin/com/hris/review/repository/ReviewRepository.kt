package com.hris.review.repository

import com.hris.review.dto.Review
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.LocalDateTime
import java.util.*

interface ReviewRepository {

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    suspend fun create(review: Review): UUID

    suspend fun read(id: UUID): Review?

    suspend fun update(id: UUID, review: Review)

    suspend fun delete(id: UUID)

    suspend fun getReviewsByEmployeeId(employeeId: Long): List<Review>

    suspend fun getReviewsByEmployeeIdBefore(employeeId: Long, date: LocalDateTime): List<Review>

    suspend fun getReviewsByEmployeeIdAfter(employeeId: Long, date: LocalDateTime): List<Review>

    suspend fun getReviewsByEmployeeIdBetween(
        employeeId: Long,
        dateAfter: LocalDateTime,
        dateBefore: LocalDateTime,
    ): List<Review>
}
