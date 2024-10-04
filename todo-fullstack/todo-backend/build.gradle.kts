plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "kz.danekerscode"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // kotlin dependencies
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation ("io.github.oshai:kotlin-logging-jvm:5.1.4")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    // ------------------------------------------------------------

    // spring dependencies
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    // ------------------------------------------------------------

    // java template engine dependencies
    implementation("gg.jte:jte:3.1.12")
    implementation("gg.jte:jte-spring-boot-starter-3:3.1.12")
    // ------------------------------------------------------------

    // test dependencies
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
