package com.hris.review.repository

import com.hris.review.dto.Review
import com.hris.review.repository.entity.Reviews
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import java.time.LocalDateTime
import java.util.*

class ReviewRepositoryImpl : ReviewRepository {

    override suspend fun create(review: Review): UUID = dbQuery {
        Reviews.insert {
            it[employeeId] = review.employeeId
            it[performance] = review.performance
            it[softSkills] = review.softSkills
            it[independence] = review.independence
            it[aspirationForGrowth] = review.aspirationForGrowth

        }[Reviews.id]
    }

    override suspend fun read(id: UUID): Review? {
        return dbQuery {
            Reviews.select { Reviews.id eq id }
                .map {
                    Review(
                        it[Reviews.id].toString(),
                        it[Reviews.employeeId],
                        it[Reviews.performance],
                        it[Reviews.softSkills],
                        it[Reviews.independence],
                        it[Reviews.aspirationForGrowth],
                        it[Reviews.createdAt].toString(),
                        it[Reviews.lastModifiedAt].toString()
                    )
                }
                .singleOrNull()
        }
    }

    override suspend fun update(id: UUID, review: Review) {
        dbQuery {
            Reviews.update({ Reviews.id eq id }) {
                it[employeeId] = review.employeeId
                it[performance] = review.performance
                it[softSkills] = review.softSkills
                it[independence] = review.independence
                it[aspirationForGrowth] = review.aspirationForGrowth
                it[lastModifiedAt] = CurrentDateTime
            }
        }
    }

    override suspend fun delete(id: UUID) {
        dbQuery {
            Reviews.deleteWhere { Reviews.id.eq(id) }
        }
    }

    override suspend fun getReviewsByEmployeeId(employeeId: Long): List<Review> {
        return dbQuery {
            Reviews.select { Reviews.employeeId eq employeeId }
                .map {
                    Review(
                        it[Reviews.id].toString(),
                        it[Reviews.employeeId],
                        it[Reviews.performance],
                        it[Reviews.softSkills],
                        it[Reviews.independence],
                        it[Reviews.aspirationForGrowth],
                        it[Reviews.createdAt].toString(),
                        it[Reviews.lastModifiedAt].toString()
                    )
                }
        }
    }

    override suspend fun getReviewsByEmployeeIdBefore(employeeId: Long, date: LocalDateTime): List<Review> {

        return dbQuery {
            Reviews.select { (Reviews.employeeId eq employeeId) and (Reviews.createdAt less date) }
                .map {
                    Review(
                        it[Reviews.id].toString(),
                        it[Reviews.employeeId],
                        it[Reviews.performance],
                        it[Reviews.softSkills],
                        it[Reviews.independence],
                        it[Reviews.aspirationForGrowth],
                        it[Reviews.createdAt].toString(),
                        it[Reviews.lastModifiedAt].toString()
                    )
                }
        }
    }

    override suspend fun getReviewsByEmployeeIdAfter(employeeId: Long, date: LocalDateTime): List<Review> {
        return dbQuery {
            Reviews.select { (Reviews.employeeId eq employeeId) and (Reviews.createdAt greater date) }
                .map {
                    Review(
                        it[Reviews.id].toString(),
                        it[Reviews.employeeId],
                        it[Reviews.performance],
                        it[Reviews.softSkills],
                        it[Reviews.independence],
                        it[Reviews.aspirationForGrowth],
                        it[Reviews.createdAt].toString(),
                        it[Reviews.lastModifiedAt].toString()
                    )
                }
        }
    }

    override suspend fun getReviewsByEmployeeIdBetween(
        employeeId: Long,
        dateAfter: LocalDateTime,
        dateBefore: LocalDateTime,
    ): List<Review> {
        return dbQuery {
            Reviews.select {
                (Reviews.employeeId eq employeeId) and
                        (Reviews.createdAt greater dateAfter) and
                        (Reviews.createdAt less dateBefore)
            }
                .map {
                    Review(
                        it[Reviews.id].toString(),
                        it[Reviews.employeeId],
                        it[Reviews.performance],
                        it[Reviews.softSkills],
                        it[Reviews.independence],
                        it[Reviews.aspirationForGrowth],
                        it[Reviews.createdAt].toString(),
                        it[Reviews.lastModifiedAt].toString()
                    )
                }
        }
    }
}
