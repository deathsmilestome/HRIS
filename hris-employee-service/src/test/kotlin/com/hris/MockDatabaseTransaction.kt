package com.hris

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
open class MockDatabaseTransaction {

    private val manager = TestTransactionManager()

    @BeforeAll
    fun setDatabase() = manager.apply()

    @AfterAll
    fun killDatabase() = manager.reset()
}
