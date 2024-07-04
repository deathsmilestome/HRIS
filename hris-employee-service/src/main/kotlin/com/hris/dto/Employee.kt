package com.hris.dto

import kotlinx.serialization.Serializable

@Serializable
data class Employee(
    val id: Long? = null,
    val name: String,
    val surname: String,
    val mail: String,
    val position: String,
    val supervisorId: Long? = null,
    val numberOfSubordinates: Int? = 0,
    val createdAt: String? = null,
    val lastModifiedAt: String? = null,
)
