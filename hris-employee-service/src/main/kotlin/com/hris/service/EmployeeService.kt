package com.hris.service

import com.hris.dto.Employee

interface EmployeeService {

    suspend fun getEmployee(id: Long): Employee?

    suspend fun createEmployee(employee: Employee): Long

    suspend fun updateEmployee(employee: Employee)

    suspend fun delete(id: Long)

    suspend fun checkEmployee(id: Long): Employee

}
