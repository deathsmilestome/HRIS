package com.hris.repository

import com.hris.dto.Employee
import com.hris.dto.HierarchyNode
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

interface EmployeeRepository {

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    suspend fun create(employee: Employee): Long

    suspend fun read(id: Long): Employee?

    suspend fun update(id: Long, employee: Employee)

    suspend fun delete(id: Long)

    suspend fun getSupervisorId(id: Long): Long?

    suspend fun getSubs(id: Long): List<HierarchyNode>

    suspend fun getSubsExceptOne(id: Long, exceptId: Long): List<HierarchyNode>

    suspend fun getEmployeeForHierarchyNode(id: Long): HierarchyNode

    suspend fun getEmployeeSubCount(id: Long): Int?

    suspend fun updateSubCount(id: Long, count: Int)

    suspend fun countSubs(id: Long): Int
}
