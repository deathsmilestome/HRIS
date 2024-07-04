package com.hris.review.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Review(
    @SerialName("id")
    var id: String? = null,
    @SerialName("employee_id")
    val employeeId: Long,
    @SerialName("performance")
    val performance: Short,
    @SerialName("soft_skills")
    val softSkills: Short,
    @SerialName("independence")
    val independence: Short,
    @SerialName("aspiration_for_growth")
    val aspirationForGrowth: Short,
    @SerialName("created_at")
    val createdAt: String? = null,
    @SerialName("last_modified_at")
    val lastModifiedAt: String? = null,
)
