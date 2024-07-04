package com.hris.service

import com.hris.dto.Employee
import com.hris.dto.HierarchyNode

interface HierarchyService {

    suspend fun getDefaultHierarchy(id: Long): HierarchyNode

    suspend fun getSubs(id: Long): List<HierarchyNode>?

    suspend fun getSupervisor(id: Long): HierarchyNode?

    suspend fun getEquals(id: Long): List<HierarchyNode>?

    suspend fun getAllToHead(id: Long): HierarchyNode

    suspend fun checkEmployee(id: Long): Employee

}
