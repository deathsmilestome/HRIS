package com.hris.review.config

import io.ktor.server.config.*
import org.slf4j.LoggerFactory

class Settings(private val configuration: ApplicationConfig) {

    val db = Db()
    val format = Format()

    companion object {
        private val logger = LoggerFactory.getLogger(Settings::class.java)
    }

    inner class Db {
        val dbUrl = getStringValueFromPropertiesFile(com.hris.review.util.DB_URL)
        val dbUser = getStringValueFromPropertiesFile(com.hris.review.util.DB_USER)
        val dbDriver = getStringValueFromPropertiesFile(com.hris.review.util.DB_DRIVER)
        val dbPassword = getStringValueFromPropertiesFile(com.hris.review.util.DB_PASSWORD)
    }

    inner class Format {
        val dateFormat = getStringValueFromPropertiesFile(com.hris.review.util.DATE_FORMAT)
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
