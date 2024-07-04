package com.hris.service

import com.hris.cache.Cache
import com.hris.dto.Employee
import com.hris.repository.EmployeeRepository
import io.ktor.server.plugins.*
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class EmployeeServiceImpl(
    private val employeeRepository: EmployeeRepository,
    private val cache: Cache<Employee>,
) : EmployeeService {

    override suspend fun getEmployee(id: Long): Employee? {
        cache.get(id.toString())?.let {
            return Json.decodeFromString(it)
        }
        return checkEmployee(id)
            .also { cache.set(it.id.toString(), it) }
    }

    override suspend fun createEmployee(employee: Employee): Long {
        return newSuspendedTransaction {
            employee.supervisorId?.let { supervisorId ->
                employeeRepository.getEmployeeSubCount(supervisorId)
                    ?.let { count -> employeeRepository.updateSubCount(supervisorId, count + 1) }
                cache.updateCache(supervisorId.toString(), checkEmployee(supervisorId))
            }
            employeeRepository.create(employee)
        }
    }

    override suspend fun updateEmployee(employee: Employee) {
        employee.id?.let {
            newSuspendedTransaction {
                checkEmployee(employee.id)
                cache.updateCache(it.toString(), employee)
                employeeRepository.update(it, employee)
            }
        }
    }

    override suspend fun delete(id: Long) {
        return newSuspendedTransaction {
            checkEmployee(id).supervisorId?.let { supervisorId ->
                employeeRepository.getEmployeeSubCount(supervisorId)
                    ?.let { count -> employeeRepository.updateSubCount(supervisorId, count - 1) }
                cache.updateCache(supervisorId.toString(), checkEmployee(supervisorId))
            }
            employeeRepository.delete(id)
            cache.delete(id.toString())
        }
    }

    override suspend fun checkEmployee(id: Long): Employee {
        return employeeRepository.read(id)
            ?: throw BadRequestException("No employee with <id>: $id in EmployeeService")
    }
}
