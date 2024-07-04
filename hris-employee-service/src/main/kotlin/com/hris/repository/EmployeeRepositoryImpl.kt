package com.hris.repository

import com.hris.dto.Employee
import com.hris.dto.HierarchyNode
import com.hris.repository.table.Employees
import io.ktor.server.plugins.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.javatime.CurrentDateTime

class EmployeeRepositoryImpl : EmployeeRepository {

    override suspend fun create(employee: Employee): Long = dbQuery {
        Employees.insert {
            it[name] = employee.name
            it[surname] = employee.surname
            it[mail] = employee.mail
            it[position] = employee.position
            it[supervisorId] = employee.supervisorId
            it[numberOfSubordinates] = employee.numberOfSubordinates ?: 0
        }[Employees.id]
    }

    override suspend fun read(id: Long): Employee? {
        return dbQuery {
            Employees.select { Employees.id eq id }
                .map {
                    Employee(
                        it[Employees.id],
                        it[Employees.name],
                        it[Employees.surname],
                        it[Employees.mail],
                        it[Employees.position],
                        it[Employees.supervisorId],
                        it[Employees.numberOfSubordinates],
                        it[Employees.createdAt].toString(),
                        it[Employees.lastModifiedAt].toString()

                    )
                }
                .singleOrNull()
        }
    }

    override suspend fun update(id: Long, employee: Employee) {
        dbQuery {
            Employees.update({ Employees.id eq id }) {
                it[name] = employee.name
                it[surname] = employee.surname
                it[mail] = employee.mail
                it[position] = employee.position
                it[supervisorId] = employee.supervisorId
                it[lastModifiedAt] = CurrentDateTime
            }
        }
    }

    override suspend fun delete(id: Long) {
        dbQuery {
            Employees.deleteWhere { Employees.id.eq(id) }
        }
    }

    override suspend fun getSupId(id: Long): Long? {
        return dbQuery {
            Employees.slice(Employees.supervisorId)
                .select { Employees.id eq id }
                .map {
                    it[Employees.supervisorId]
                }
                .singleOrNull()
        }
    }

    override suspend fun getSubs(id: Long): List<HierarchyNode> {
        return dbQuery {
            Employees.slice(Employees.id, Employees.name, Employees.surname, Employees.position)
                .select { Employees.supervisorId eq id }
                .map {
                    HierarchyNode(
                        it[Employees.id].toString(),
                        it[Employees.name],
                        it[Employees.surname],
                        it[Employees.position],
                    )
                }
        }
    }

    override suspend fun getSubsExceptOne(id: Long, exceptId: Long): List<HierarchyNode> {
        return dbQuery {
            Employees.slice(Employees.id, Employees.name, Employees.surname, Employees.position)
                .select { (Employees.supervisorId eq id) and (Employees.id neq exceptId) }
                .map {
                    HierarchyNode(
                        it[Employees.id].toString(),
                        it[Employees.name],
                        it[Employees.surname],
                        it[Employees.position],
                    )
                }
        }
    }

    override suspend fun getEmployeeAsHierarchyNode(id: Long): HierarchyNode {
        return dbQuery {
            Employees.select { Employees.id eq id }
                .map {
                    HierarchyNode(
                        it[Employees.id].toString(),
                        it[Employees.name],
                        it[Employees.surname],
                        it[Employees.position],
                    )
                }
                .singleOrNull() ?: throw BadRequestException("No employee with <id>: $id")
        }
    }

    override suspend fun getEmployeeSubCount(id: Long): Int? {
        return dbQuery {
            Employees.slice(Employees.numberOfSubordinates)
                .select { Employees.id eq id }
                .map {
                    it[Employees.numberOfSubordinates]
                }
                .singleOrNull()
        }
    }

    override suspend fun updateSubCount(id: Long, count: Int) {
        dbQuery {
            Employees.update({ Employees.id eq id }) {
                it[numberOfSubordinates] = count
            }
        }
    }

    override suspend fun countSubs(id: Long): Int {
        return dbQuery {
            Employees.slice(Employees.id)
                .select { Employees.supervisorId eq id }
                .count().toInt()
        }
    }
}
