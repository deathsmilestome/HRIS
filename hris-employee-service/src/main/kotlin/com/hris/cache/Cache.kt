package com.hris.cache

interface Cache<T> {
    suspend fun get(key: String): String?

    suspend fun set(key: String, value: T)

    suspend fun updateCache(key: String, value: T)

    suspend fun delete(key: String)
}
