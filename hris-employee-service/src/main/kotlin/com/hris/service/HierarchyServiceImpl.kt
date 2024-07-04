package com.hris.service

import com.hris.dto.Employee
import com.hris.dto.HierarchyNode
import com.hris.dto.toHierarchyNode
import com.hris.repository.EmployeeRepository
import io.ktor.server.plugins.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class HierarchyServiceImpl(
    private val employeeRepository: EmployeeRepository,
) : HierarchyService {

    // my boss + me + my subs
    override suspend fun getDefaultHierarchy(id: Long): HierarchyNode {
        return newSuspendedTransaction {
            val employee = employeeRepository.getEmployeeForHierarchyNode(id)
            employee.apply { this.subs.addAll(employeeRepository.getSubs(id)) }
            val supervisor = employeeRepository.getSupervisorId(id)?.let {
                employeeRepository.getEmployeeForHierarchyNode(it).apply { this.subs.add(employee) }
            }
            return@newSuspendedTransaction supervisor ?: employee
        }
    }

    override suspend fun getSupervisor(id: Long): HierarchyNode? {
        return checkEmployee(id).supervisorId?.let { employeeRepository.getEmployeeForHierarchyNode(it) }
    }


    override suspend fun getSubs(id: Long): List<HierarchyNode>? =
        newSuspendedTransaction {
            checkEmployee(id)
            employeeRepository.getSubs(id).ifEmpty { null }
        }

    // all subs of id's supervisor
    override suspend fun getEquals(id: Long): List<HierarchyNode>? {
        return newSuspendedTransaction {
            checkEmployee(id)
            val supervisorId = employeeRepository.getSupervisorId(id) ?: return@newSuspendedTransaction null
            val equals = employeeRepository.getSubsExceptOne(supervisorId, id)
            return@newSuspendedTransaction equals.ifEmpty { null }
        }
    }

    override suspend fun getAllToHead(id: Long): HierarchyNode {
        return newSuspendedTransaction {
            var tempHierarchyNode = employeeRepository.getEmployeeForHierarchyNode(id)
            var tempId: Long? = id
            while (tempId != null) {
                tempId = employeeRepository.getSupervisorId(tempId) ?: break
                tempHierarchyNode = employeeRepository.getEmployeeForHierarchyNode(tempId).apply { this.subs.add(tempHierarchyNode) }
            }
            return@newSuspendedTransaction tempHierarchyNode
        }
    }

    override suspend fun checkEmployee(id: Long): Employee {
        return employeeRepository.read(id)
            ?: throw BadRequestException("No employee with <id>: $id in HierarchyService")
    }
}
