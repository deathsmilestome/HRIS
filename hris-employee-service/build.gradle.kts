val kotlin_version: String by project
val logback_version: String by project
val exposed_version: String by project
val kodein_version: String by project
val kredisVersion: String by project
val junit_jupiter_version: String by project
val testconteiner_version: String by project
val mockk_version: String by project
val swagger_version: String by project

plugins {
    kotlin("jvm") version "2.0.0"
    id("io.ktor.plugin") version "2.3.12"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

version = "0.0.1"

application {
    mainClass.set("com.hris.MainKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

tasks.test {
    useJUnitPlatform()
}

dependencies {

    // Ktor
    implementation("io.ktor:ktor-server-status-pages")
    implementation("io.ktor:ktor-server-config-yaml")
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("io.ktor:ktor-server-openapi")

    // Koidein
    implementation("org.kodein.di:kodein-di-framework-ktor-server-jvm:$kodein_version")

    // Exposed
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposed_version")
    implementation("org.postgresql:postgresql:42.7.2")

    // Redis
    api("io.github.crackthecodeabhi:kreds:$kredisVersion")

    // Logging
    implementation("ch.qos.logback:logback-classic:$logback_version")

    // Swagger
    implementation("io.github.smiley4:ktor-swagger-ui:$swagger_version")

    // Tests
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    testImplementation("io.ktor:ktor-server-test-host")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:$junit_jupiter_version")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junit_jupiter_version")
    testImplementation("org.testcontainers:testcontainers:$testconteiner_version")
    testImplementation("org.testcontainers:postgresql:$testconteiner_version")
    testImplementation("org.testcontainers:testcontainers:$testconteiner_version")
    testImplementation("io.mockk:mockk:${mockk_version}")

}
