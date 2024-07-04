package com.hris.review.repository.entity

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object Reviews : Table() {
    val id = uuid("id").autoGenerate()
    val employeeId = long("employee_id")
    val performance = short("performance")
    val softSkills = short("soft_skills")
    val independence = short("independence")
    val aspirationForGrowth = short("aspiration_for_growth")
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    val lastModifiedAt = datetime("last_modified_at").defaultExpression(CurrentDateTime)

    override val primaryKey = PrimaryKey(id)
}
