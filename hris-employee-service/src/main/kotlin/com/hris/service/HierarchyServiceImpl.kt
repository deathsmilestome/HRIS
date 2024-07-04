package com.hris.service

import com.hris.dto.Employee
import com.hris.dto.HierarchyNode
import com.hris.repository.EmployeeRepository
import io.ktor.server.plugins.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class HierarchyServiceImpl(
    private val employeeRepository: EmployeeRepository,
) : HierarchyService {

    override suspend fun getDefaultHierarchy(id: Long): HierarchyNode {
        return newSuspendedTransaction {
            val employee = employeeRepository.getEmployeeAsHierarchyNode(id)
            employee.apply { this.subs.addAll(employeeRepository.getSubs(id)) }
            val supervisor = employeeRepository.getSupId(id)?.let {
                employeeRepository.getEmployeeAsHierarchyNode(it).apply { this.subs.add(employee) }
            }
            return@newSuspendedTransaction supervisor ?: employee
        }
    }

    override suspend fun getSup(id: Long): HierarchyNode? {
        return checkEmployee(id).supervisorId?.let { employeeRepository.getEmployeeAsHierarchyNode(it) }
    }

    override suspend fun getSubs(id: Long): List<HierarchyNode>? =
        newSuspendedTransaction {
            checkEmployee(id)
            employeeRepository.getSubs(id).ifEmpty { null }
        }

    override suspend fun getEqualEmployeesOnSameLevel(id: Long): List<HierarchyNode>? {
        return newSuspendedTransaction {
            checkEmployee(id)
            val supervisorId = employeeRepository.getSupId(id) ?: return@newSuspendedTransaction null
            val equals = employeeRepository.getSubsExceptOne(supervisorId, id)
            return@newSuspendedTransaction equals.ifEmpty { null }
        }
    }

    override suspend fun getAllToHead(id: Long): HierarchyNode {
        return newSuspendedTransaction {
            var tempHierarchyNode = employeeRepository.getEmployeeAsHierarchyNode(id)
            var tempId: Long? = id
            while (tempId != null) {
                tempId = employeeRepository.getSupId(tempId) ?: break
                tempHierarchyNode = employeeRepository.getEmployeeAsHierarchyNode(tempId).apply { this.subs.add(tempHierarchyNode) }
            }
            return@newSuspendedTransaction tempHierarchyNode
        }
    }

    override suspend fun checkEmployee(id: Long): Employee {
        return employeeRepository.read(id)
            ?: throw BadRequestException("No employee with <id>: $id in HierarchyService")
    }
}
