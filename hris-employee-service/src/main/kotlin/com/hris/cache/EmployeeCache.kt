package com.hris.cache

import com.hris.dto.Employee
import io.github.crackthecodeabhi.kreds.args.GetExOption
import io.github.crackthecodeabhi.kreds.args.SetOption
import io.github.crackthecodeabhi.kreds.connection.KredsClient
import kotlinx.serialization.json.Json

class EmployeeCache(
    private val redis: KredsClient,
    private val ttl: ULong,
) : Cache<Employee> {

    override suspend fun get(key: String): String? {
        return redis.getEx(key = key, getExOption = GetExOption.Builder(exSeconds = ttl).build())
    }

    override suspend fun set(key: String, value: Employee) {
        redis.set(
            key = key,
            value = Json.encodeToString(Employee.serializer(), value),
            setOption = SetOption.Builder(exSeconds = ttl).build()
        )
    }

    override suspend fun updateCache(key: String, value: Employee) {
        redis.get(key)?.let { set(key, value) }
    }

    override suspend fun delete(key: String) {
        redis.del(key)
    }
}
