package com.hris.unit

import com.hris.MockDatabaseTransaction
import com.hris.TestTransactionManager
import com.hris.cache.Cache
import com.hris.dto.Employee
import com.hris.repository.EmployeeRepositoryImpl
import com.hris.service.EmployeeServiceImpl
import io.ktor.server.plugins.*
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.test.assertEquals

@Tag("EmployeeServiceTest")
class EmployeeServiceTest: MockDatabaseTransaction() {
    private val basicEmployee = Employee(
        id = 1L,
        name = "Tyler",
        surname = "Durden",
        mail = "tayler@project.debacle",
        position = "none",
        supervisorId = 1L,
        numberOfSubordinates = 1,
        createdAt = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.uuuuuu")).toString(),
        lastModifiedAt = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.uuuuuu")).toString()
    )


    @Test
    fun testCheckEmployee() {
        val employeeRepository = mockk<EmployeeRepositoryImpl>()
        val cache = mockk<Cache<Employee>>()
        val employeeService = EmployeeServiceImpl(employeeRepository, cache)
        coEvery { employeeRepository.read(1L) } returns basicEmployee
        coEvery { employeeRepository.read(2L) } returns null

        runBlocking {
            assertEquals(basicEmployee, employeeService.checkEmployee(1L))
            assertThrows<BadRequestException> { employeeService.checkEmployee(2L) }
        }
    }


    @Test
    fun testGetEmployee() {
        val employeeRepository = mockk<EmployeeRepositoryImpl>()
        val cache = mockk<Cache<Employee>>()
        val employeeService = EmployeeServiceImpl(employeeRepository, cache)

        coEvery { cache.set("1", basicEmployee) } returns mockk()
        coEvery { cache.get(key = "1") } returns Json.encodeToString(Employee.serializer(), basicEmployee)
        coEvery { cache.get(key = "2") } returns null
        coEvery { employeeRepository.read(2L) } returns basicEmployee

        runBlocking {
            // From cache
            assertEquals(basicEmployee, employeeService.getEmployee(1))
            // From database and save to cache
            assertEquals(basicEmployee, employeeService.getEmployee(2))
            coVerify(exactly = 1) { cache.set("1", basicEmployee) }
        }
    }

    @Test
    fun testCreateEmployee() {

        val employeeRepository = mockk<EmployeeRepositoryImpl>()
        val cache = mockk<Cache<Employee>>()
        val employeeService = EmployeeServiceImpl(employeeRepository, cache)
        coEvery { cache.updateCache("1", basicEmployee) } returns mockk()
        coEvery { employeeRepository.getEmployeeSubCount(1L) } returns 1
        coEvery { employeeRepository.updateSubCount(1L, 2) } returns mockk()
        coEvery { employeeRepository.read(1L) } returns basicEmployee
        coEvery { employeeRepository.create(basicEmployee) } returns 1L

        // Create employee and update cache for hi supervisor's sub count
        runBlocking {
            assertEquals(1L, employeeService.createEmployee(basicEmployee))
            coVerify(exactly = 1) { cache.updateCache("1", basicEmployee) }
        }

    }

    @Test
    fun testUpdateEmployee() {

        val employeeRepository = mockk<EmployeeRepositoryImpl>()
        val cache = mockk<Cache<Employee>>()
        val employeeService = EmployeeServiceImpl(employeeRepository, cache)
        coEvery { cache.updateCache("1", basicEmployee) } returns mockk()
        coEvery { employeeRepository.read(1L) } returns basicEmployee
        coEvery { employeeRepository.update(1L, basicEmployee) } returns mockk()
        coEvery { employeeRepository.update(2L, basicEmployee) } returns mockk()
        coEvery { employeeRepository.read(2L) } returns null


        runBlocking {
            // Update employee and cache
            employeeService.updateEmployee(basicEmployee)
            coVerify(exactly = 1) { cache.updateCache("1", basicEmployee) }

            assertThrows<BadRequestException> { employeeService.checkEmployee(2L) }
        }
    }


    @Test
    fun testDeleteEmployee() {

        val employeeRepository = mockk<EmployeeRepositoryImpl>()
        val cache = mockk<Cache<Employee>>()
        val employeeService = EmployeeServiceImpl(employeeRepository, cache)

        coEvery { cache.updateCache("1", basicEmployee) } returns mockk()
        coEvery { cache.delete("1") } returns mockk()
        coEvery { employeeRepository.getEmployeeSubCount(1L) } returns 1
        coEvery { employeeRepository.updateSubCount(1L, 0) } returns mockk()
        coEvery { employeeRepository.read(1L) } returns basicEmployee
        coEvery { employeeRepository.delete(1L) } returns mockk()

        runBlocking {
            // Delete employee and update cache for hi supervisor's sub count
            employeeService.delete(1L)
            coVerify(exactly = 1) { cache.updateCache("1", basicEmployee) }
        }
    }
}
