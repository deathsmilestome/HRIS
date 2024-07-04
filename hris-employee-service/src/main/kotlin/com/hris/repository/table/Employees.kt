package com.hris.repository.table

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object Employees : Table() {
    val id = long("id").autoIncrement()
    val name = varchar("name", length = 50)
    val surname = varchar("surname", length = 50)
    val mail = varchar("mail", length = 50)
    val position = varchar("position", length = 50)
    val supervisorId = long("supervisor_id").nullable()
    val numberOfSubordinates = integer("number_of_subordinates")
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    val lastModifiedAt = datetime("last_modified_at").defaultExpression(CurrentDateTime)

    // TODO описать создание индекса в доке к БД
    override val primaryKey = PrimaryKey(id)
}
