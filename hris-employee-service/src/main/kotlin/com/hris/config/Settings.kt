package com.hris.config

import com.hris.util.*
import io.ktor.server.config.*
import org.slf4j.LoggerFactory

class Settings(private val configuration: ApplicationConfig) {

    val db = Db()
    val redis = Redis()

    companion object {
        private val logger = LoggerFactory.getLogger(Settings::class.java)
    }

    inner class Db {
        val dbUrl = getStringValueFromPropertiesFile(DB_URL)
        val dbUser = getStringValueFromPropertiesFile(DB_USER)
        val dbDriver = getStringValueFromPropertiesFile(DB_DRIVER)
        val dbPassword = getStringValueFromPropertiesFile(DB_PASSWORD)
    }

    inner class Redis {
        val redisHost = getStringValueFromPropertiesFile(REDIS_HOST)
        val redisPort = getStringValueFromPropertiesFile(REDIS_PORT)
        val ttl = getStringValueFromPropertiesFile(REDIS_TTL)
    }

    fun getStringValueFromPropertiesFile(field: String): String {
        val value = configuration.propertyOrNull(field)
        if (value == null) {
            logger.error("Incorrect property field: [$field].")
            throw Exception("Failed create instance of ${this.javaClass.name}")
        }
        val stringValue = value.getString()
        logger.trace("Property [$field] = [${stringValue}]")
        return stringValue
    }

    fun getListValuesFromPropertiesFile(field: String, regex: Regex): List<String> {
        return getStringValueFromPropertiesFile(field).trim().split(regex)
    }
}
