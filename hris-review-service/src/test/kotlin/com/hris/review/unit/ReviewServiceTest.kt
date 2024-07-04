package com.hris.review.unit

import com.hris.review.di.settingsModule
import com.hris.review.dto.Review
import com.hris.review.repository.ReviewRepository
import com.hris.review.service.ReviewServiceImpl
import com.hris.review.util.Utils
import io.ktor.server.config.*
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.kodein.di.DI
import org.kodein.di.instance
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNull

@Tag("ReviewServiceTest")
class ReviewServiceTest {

    private val di = DI { import(settingsModule(ApplicationConfig("application-test.yaml"))) }
    private val settings: com.hris.review.config.Settings by di.instance()
    private val utils = Utils(settings)
    private val testReview = Review(
        "0346d569-6e42-4b6c-8197-a04446bd729e",
        1,
        1,
        1,
        1,
        1,
        getNow(),
        getNow(),
    )

    private fun getNow() = LocalDateTime.now().format(DateTimeFormatter.ofPattern(utils.dateFormat))
    private fun getToday() = LocalDateTime.now().format(DateTimeFormatter.ofPattern(utils.dateFormatForReviewsFilter))
    val today = utils.getDateFromString(getToday())

    @Test
    fun testGetReview() {
        val reviewRepository = mockk<ReviewRepository>()
        val reviewService = ReviewServiceImpl(reviewRepository)


        coEvery { reviewRepository.read(UUID.fromString(testReview.id)) } returns testReview
        coEvery { reviewRepository.read(UUID.fromString("0346d569-6e42-4b6c-8197-a04446bd729f")) } returns null

        runBlocking {
            assertEquals(testReview, reviewService.getReview(UUID.fromString(testReview.id)))
            assertNull(reviewService.getReview(UUID.fromString("0346d569-6e42-4b6c-8197-a04446bd729f")))
        }
    }

    @Test
    fun testGetReviewsByEmployeeId() {
        val reviewRepository = mockk<ReviewRepository>()
        val reviewService = ReviewServiceImpl(reviewRepository)


        coEvery { reviewRepository.getReviewsByEmployeeId(1L) } returns listOf(testReview)
        coEvery { reviewRepository.getReviewsByEmployeeId(2L) } returns listOf()

        runBlocking {
            assertEquals(listOf(testReview), reviewService.getReviewsByEmployeeId(testReview.employeeId))
            assertNull(reviewService.getReviewsByEmployeeId(2L))
        }
    }

    @Test
    fun testGetReviewsByEmployeeIdBefore() {
        val reviewRepository = mockk<ReviewRepository>()
        val reviewService = ReviewServiceImpl(reviewRepository)


        coEvery { reviewRepository.getReviewsByEmployeeIdBefore(1L, today) } returns listOf(testReview)
        coEvery { reviewRepository.getReviewsByEmployeeIdBefore(2L, today) } returns listOf()

        runBlocking {
            assertEquals(listOf(testReview), reviewService.getReviewsByEmployeeIdBefore(1L, today))
            assertNull(reviewService.getReviewsByEmployeeIdBefore(2L, today))
        }
    }

    @Test
    fun testGetReviewsByEmployeeIdAfter() {
        val reviewRepository = mockk<ReviewRepository>()
        val reviewService = ReviewServiceImpl(reviewRepository)


        coEvery { reviewRepository.getReviewsByEmployeeIdAfter(1L, today) } returns listOf(testReview)
        coEvery { reviewRepository.getReviewsByEmployeeIdAfter(2L, today) } returns listOf()

        runBlocking {
            assertEquals(listOf(testReview), reviewService.getReviewsByEmployeeIdAfter(1L, today))
            assertNull(reviewService.getReviewsByEmployeeIdAfter(2L, today))
        }
    }

    @Test
    fun testGetReviewsByEmployeeIdBetween() {
        val reviewRepository = mockk<ReviewRepository>()
        val reviewService = ReviewServiceImpl(reviewRepository)


        coEvery { reviewRepository.getReviewsByEmployeeIdBetween(1L, today, today) } returns listOf(testReview)
        coEvery { reviewRepository.getReviewsByEmployeeIdBetween(2L, today, today) } returns listOf()

        runBlocking {
            assertEquals(listOf(testReview), reviewService.getReviewsByEmployeeIdBetween(1L, today, today))
            assertNull(reviewService.getReviewsByEmployeeIdBetween(2L, today, today))
        }
    }

    @Test
    fun testUpdateReview() {
        val reviewRepository = mockk<ReviewRepository>()
        val reviewService = ReviewServiceImpl(reviewRepository)

        runBlocking {
            assertThrows<IllegalArgumentException> {
                reviewService.updateReview(
                    testReview.copy().apply { this.id = "invalid UUID" })
            }
        }
    }
}