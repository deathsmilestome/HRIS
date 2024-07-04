package com.hris.dto

import kotlinx.serialization.Serializable

@Serializable
data class HierarchyNode(
    val id: String,
    val name: String,
    val surname: String,
    val position: String,
    var subs: MutableList<HierarchyNode> = mutableListOf(),
)
