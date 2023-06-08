val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val exposed_version: String by project
val postgres_version: String by project
val hikari_version: String by project
val dagger_version: String by project

plugins {
    kotlin("jvm") version "1.8.10"
    kotlin("plugin.serialization") version "1.8.10"
    id("io.ktor.plugin") version "2.2.3"
    id("org.jetbrains.kotlin.kapt") version "1.8.20-Beta"
}

group = "ru.noteon"
version = "0.0.1"
application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")

    // CORS
    implementation("io.ktor:ktor-server-cors:$ktor_version")

    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")

    implementation("io.ktor:ktor-server-status-pages:$ktor_version")

    implementation("io.ktor:ktor-server-auth:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jwt:$ktor_version")

    // Exposed
    implementation ("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation ("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation ("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation ("org.jetbrains.exposed:exposed-jodatime:$exposed_version")

    // PostgreSQL
    implementation ("org.postgresql:postgresql:$postgres_version")

    implementation ("com.zaxxer:HikariCP:$hikari_version")

    // Dagger
    implementation ("com.google.dagger:dagger:$dagger_version")
    kapt ("com.google.dagger:dagger-compiler:$dagger_version")

    implementation("org.mindrot:jbcrypt:0.4")
}