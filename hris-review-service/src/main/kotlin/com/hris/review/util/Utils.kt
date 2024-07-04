package com.hris.review.util

import com.hris.review.config.Settings
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Utils(
    settings: Settings,
) {
    val dateFormat = settings.format.dateFormat
    val dateFormatForReviewsFilter = settings.format.dateFormatForReviewFilter
    fun getDateFromString(s: String?): LocalDateTime {
        return LocalDateTime.parse("$s-00:00", DateTimeFormatter.ofPattern(dateFormat))
    }
}
