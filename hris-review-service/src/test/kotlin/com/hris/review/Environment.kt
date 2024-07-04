package com.hris.review

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
open class Environment {


    private lateinit var reviewsDb: PostgreSQLContainer<Nothing>


    @BeforeAll
    fun createEnvironment() {
        reviewsDb = PostgreSQLContainer<Nothing>(DockerImageName.parse("postgres:13.3")).apply {
            this.withDatabaseName("reviews")
            this.withUsername("dstm")
            this.withPassword("dstm")
            setPortBindings(listOf("5435:5432"))
        }
        reviewsDb.start()
    }

    @AfterAll
    fun shutdown() {
        reviewsDb.stop()
    }
}

