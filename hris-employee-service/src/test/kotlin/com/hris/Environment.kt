package com.hris

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
open class Environment {


    private lateinit var employeesDb: PostgreSQLContainer<Nothing>
    private lateinit var redisCache: GenericContainer<Nothing>


    @BeforeAll
    fun createEnvironment() {
        employeesDb = PostgreSQLContainer<Nothing>(DockerImageName.parse("postgres:13.3")).apply {
            this.withDatabaseName("employees")
            this.withUsername("dstm")
            this.withPassword("dstm")
            setPortBindings(listOf("5434:5432"))
        }
        employeesDb.start()

        redisCache =  GenericContainer<Nothing>(DockerImageName.parse("redis:6-alpine")).apply {
            setPortBindings(listOf("6379:6379"))
        }
        redisCache.start()
    }

    @AfterAll
    fun shutdown() {
        employeesDb.stop()
        redisCache.stop()
    }
}

