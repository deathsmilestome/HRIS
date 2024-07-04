package com.hris.unit

import com.hris.MockDatabaseTransaction
import com.hris.cache.Cache
import com.hris.dto.Employee
import com.hris.dto.HierarchyNode
import com.hris.dto.toHierarchyNode
import com.hris.repository.EmployeeRepositoryImpl
import com.hris.service.EmployeeServiceImpl
import com.hris.service.HierarchyServiceImpl
import io.ktor.server.plugins.*
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertNull

@Tag("HierarchyServiceTest")
class HierarchyServiceTest : MockDatabaseTransaction() {

    val employeeSup = HierarchyNode("1", "name", "surname", "position")
    val employee = HierarchyNode("2", "name", "surname", "position")
    val employeeFirstSub = HierarchyNode("3", "name", "surname", "position")
    val employeeSecondSub = HierarchyNode("4", "name", "surname", "position")

    @Test
    fun testGetDefaultHierarchy() {
        val employeeRepository = mockk<EmployeeRepositoryImpl>()
        val hierarchyService = HierarchyServiceImpl(employeeRepository)

        coEvery { employeeRepository.getEmployeeForHierarchyNode(2L) } returns employee
        coEvery { employeeRepository.getSubs(2L) } returns listOf(employeeFirstSub)
        coEvery { employeeRepository.getSupervisorId(2L) } returns 1L
        coEvery { employeeRepository.getEmployeeForHierarchyNode(1L) } returns employeeSup

        coEvery { employeeRepository.getSupervisorId(1L) } returns null
        coEvery { employeeRepository.getSubs(1L) } returns listOf(employee)

        runBlocking {
            assertEquals(
                employeeSup.copy().apply { this.subs.add(employee.copy().apply { this.subs.add(employeeFirstSub) }) },
                hierarchyService.getDefaultHierarchy(2L)
            )
            assertEquals(
                employeeSup.copy().apply { this.subs.add(employee) },
                hierarchyService.getDefaultHierarchy(1L)
            )
        }
    }

    @Test
    fun testGetSupervisor() {
        val employeeRepository = mockk<EmployeeRepositoryImpl>()
        val hierarchyService = HierarchyServiceImpl(employeeRepository)

        coEvery { hierarchyService.checkEmployee(2L).supervisorId } returns 1L
        coEvery { employeeRepository.getEmployeeForHierarchyNode(1L) } returns employeeSup

        coEvery { hierarchyService.checkEmployee(1L).supervisorId } returns null

        runBlocking {
            assertEquals(employeeSup, hierarchyService.getSupervisor(2L))
            assertNull(hierarchyService.getSupervisor(1L))
        }
    }

    @Test
    fun testGetSubs() {
        val employeeRepository = mockk<EmployeeRepositoryImpl>()
        val hierarchyService = HierarchyServiceImpl(employeeRepository)

        coEvery { hierarchyService.checkEmployee(1L) } returns mockk()
        coEvery { employeeRepository.getSubs(1L) } returns listOf(employee)

        coEvery { hierarchyService.checkEmployee(3L) } returns mockk()
        coEvery { employeeRepository.getSubs(3L) } returns listOf()

        runBlocking {
            assertEquals(listOf(employee), hierarchyService.getSubs(1L))
            assertNull(hierarchyService.getSubs(3L))
        }
    }

    @Test
    fun testGetEquals() {
        val employeeRepository = mockk<EmployeeRepositoryImpl>()
        val hierarchyService = HierarchyServiceImpl(employeeRepository)

        coEvery { hierarchyService.checkEmployee(3L) } returns mockk()
        coEvery { employeeRepository.getSupervisorId(3L) } returns 2
        coEvery { employeeRepository.getSubsExceptOne(2L, 3L) } returns listOf(employeeSecondSub)

        coEvery { hierarchyService.checkEmployee(1L) } returns mockk()
        coEvery { employeeRepository.getSupervisorId(1L) } returns null

        coEvery { hierarchyService.checkEmployee(2L) } returns mockk()
        coEvery { employeeRepository.getSupervisorId(2L) } returns 4L
        coEvery { employeeRepository.getSubsExceptOne(4L, 2L) } returns listOf()

        runBlocking {
            assertEquals(listOf(employeeSecondSub), hierarchyService.getEquals(3L))
            assertNull(hierarchyService.getEquals(1L))
            assertNull(hierarchyService.getEquals(2L))
        }
    }

    @Test
    fun testGetAllToHead() {
        val employeeRepository = mockk<EmployeeRepositoryImpl>()
        val hierarchyService = HierarchyServiceImpl(employeeRepository)

        coEvery { employeeRepository.getSupervisorId(3L) } returns 2L
        coEvery { employeeRepository.getSupervisorId(2L) } returns 1L
        coEvery { employeeRepository.getSupervisorId(1L) } returns null
        coEvery { employeeRepository.getEmployeeForHierarchyNode(1L)} returns employeeSup
        coEvery { employeeRepository.getEmployeeForHierarchyNode(2L) } returns employee
        coEvery { employeeRepository.getEmployeeForHierarchyNode(3L) } returns employeeFirstSub



        runBlocking {
            assertEquals(
                employeeSup.copy().apply { this.subs.add(employee.copy().apply { this.subs.add(employeeFirstSub) }) },
                hierarchyService.getAllToHead(3L)
            )
            assertEquals(employeeSup, hierarchyService.getAllToHead(1L))
        }
    }

    @Test
    fun testCheckEmployee() {
        val employeeRepository = mockk<EmployeeRepositoryImpl>()
        val basicEmployee = mockk<Employee>()
        val hierarchyService = HierarchyServiceImpl(employeeRepository)
        coEvery { employeeRepository.read(1L) } returns basicEmployee
        coEvery { employeeRepository.read(2L) } returns null

        runBlocking {
            assertEquals(basicEmployee, hierarchyService.checkEmployee(1L))
            assertThrows<BadRequestException> { hierarchyService.checkEmployee(2L) }
        }
    }
}