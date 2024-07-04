package com.hris


import com.hris.dto.Employee
import com.hris.dto.HierarchyNode
import com.hris.repository.EmployeeRepository
import io.ktor.server.application.*
import kotlinx.coroutines.runBlocking
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import kotlin.properties.Delegates

var testEmployeeSupId by Delegates.notNull<Long>()
var testEmployeeSup = Employee(null, "name", "surname", "mail", "position")

var testEmployeeId by Delegates.notNull<Long>()
lateinit var testEmployee: Employee

var testEmployeeFirstSubId by Delegates.notNull<Long>()
lateinit var testEmployeeFirstSub: Employee
var testEmployeeSecondSubId by Delegates.notNull<Long>()
lateinit var testEmployeeSecondSub: Employee

lateinit var defaultHierarchyNodeForEmployee: HierarchyNode
lateinit var hierarchyNodeForHeadEndpoint: HierarchyNode

fun Application.initTestData() {
    val db: EmployeeRepository by closestDI().instance()

    runBlocking {
        testEmployeeSupId = db.create(testEmployeeSup)
        testEmployee = Employee(null, "name", "surname", "mail", "position", testEmployeeSupId)
        testEmployeeId = db.create(testEmployee)
        testEmployeeFirstSub = Employee(null, "name", "surname", "mail", "position", testEmployeeId)
        testEmployeeFirstSubId = db.create(testEmployeeFirstSub)
        testEmployeeSecondSub = Employee(null, "name", "surname", "mail", "position", testEmployeeId)
        testEmployeeSecondSubId = db.create(testEmployeeFirstSub)

        testEmployeeSup = db.read(testEmployeeSupId) ?: throw Exception("No testEmployeeSup testcontainer")
        testEmployee = db.read(testEmployeeId) ?: throw Exception("No testEmployee testcontainer")
        testEmployeeFirstSub = db.read(testEmployeeFirstSubId) ?: throw Exception("No testEmployeeSub testcontainer")
        testEmployeeSecondSub = db.read(testEmployeeSecondSubId) ?: throw Exception("No testEmployeeSub testcontainer")
    }

    val firstSubNode = HierarchyNode(
        testEmployeeFirstSub.id.toString(),
        testEmployeeFirstSub.name,
        testEmployeeFirstSub.surname,
        testEmployeeFirstSub.position
    )
    val secondSubNode = HierarchyNode(
        testEmployeeSecondSub.id.toString(),
        testEmployeeSecondSub.name,
        testEmployeeSecondSub.surname,
        testEmployeeSecondSub.position
    )
    val employeeNode = HierarchyNode(
        testEmployee.id.toString(),
        testEmployee.name,
        testEmployee.surname,
        testEmployee.position
    )
    val supNode = HierarchyNode(
        testEmployeeSup.id.toString(),
        testEmployeeSup.name,
        testEmployeeSup.surname,
        testEmployeeSup.position
    )
//                          ---------------------------------------------------------------------
//                                     DEFAULT HIERARCHY WHEN CALLED FOR <testEmployee>
//                          ---------------------------------------------------------------------
//
//                                                 testEmployeeSup
//                                                        |
//                                                   testEmployee
//                                                   /           \
//                                    testEmployeeFirstSub     testEmployeeSecondSub
//
//                          ---------------------------------------------------------------------
    defaultHierarchyNodeForEmployee = supNode.apply {
        this.subs.add(employeeNode.apply {
            this.subs.addAll(
                listOf(
                    firstSubNode,
                    secondSubNode
                )
            )
        }
        )
    }

    val employeeNodeForHead = HierarchyNode(
        testEmployee.id.toString(),
        testEmployee.name,
        testEmployee.surname,
        testEmployee.position
    )
    val supNodeForHead = HierarchyNode(
        testEmployeeSup.id.toString(),
        testEmployeeSup.name,
        testEmployeeSup.surname,
        testEmployeeSup.position
    )
//                          ---------------------------------------------------------------------
//                                     TO HEAD HIERARCHY WHEN CALLED FOR <testEmployeeFirstSub>
//                          ---------------------------------------------------------------------
//
//                                                 testEmployeeSup
//                                                        |
//                                                   testEmployee
//                                                        |
//                                               testEmployeeFirstSub
//
//                          ---------------------------------------------------------------------
    hierarchyNodeForHeadEndpoint = supNodeForHead.apply {
        this.subs.add(employeeNodeForHead.apply {
            this.subs.add(firstSubNode)
        }
        )
    }

}