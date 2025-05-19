println("SENTRY_AUTH_TOKEN is set: ${System.getenv("SENTRY_AUTH_TOKEN") != null}")

plugins {
    application
    checkstyle
    jacoco
    id("org.springframework.boot") version "3.4.5"
    id("io.spring.dependency-management") version "1.1.7"
    id("io.freefair.lombok") version "8.6"
    id("org.sonarqube") version "6.2.0.5505"
    id("io.sentry.jvm.gradle") version "5.6.0"
}

group = "hexlet.code"
version = "0.0.1-SNAPSHOT"

sonar {
    properties {
        property("sonar.projectKey", "andreykokorev_dangerwind")
        property("sonar.organization", "andreykokorev")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}

sentry {
    includeSourceContext = true

    org = "andrei-3n"
    projectName = "java-spring-boot"
    authToken = System.getenv("SENTRY_AUTH_TOKEN")
    println("!!!!!! ----- SENTRY_AUTH_TOKEN is set: ${System.getenv("SENTRY_AUTH_TOKEN") != null}")
}

tasks.withType<JavaExec>().configureEach {
    systemProperty("SENTRY_AUTH_TOKEN", System.getProperty("SENTRY_AUTH_TOKEN"))
}

application {
    mainClass.set("hexlet.code.AppApplication")
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.8")

    implementation("org.springframework.boot:spring-boot-configuration-processor")

    implementation("org.openapitools:jackson-databind-nullable:0.2.6")

    implementation("org.instancio:instancio-junit:5.0.2")
    implementation("net.javacrumbs.json-unit:json-unit-assertj:4.0.0")
    implementation("net.datafaker:datafaker:2.4.3")

    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    testImplementation("org.springframework.security:spring-security-test")

    implementation("org.springframework.boot:spring-boot-starter-actuator")
    // !!!!!!!!!!
        // implementation("org.springframework.security:spring-security-core:6.0.3")


    implementation("org.mapstruct:mapstruct:1.6.3")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")

    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")



    runtimeOnly("com.h2database:h2")           // Для разработки
    runtimeOnly("org.postgresql:postgresql")   // Для продакшена


    implementation("org.springframework.boot:spring-boot-starter")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation(platform("org.junit:junit-bom:5.12.0"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.12.0")

// для отладки
    implementation("org.springframework.boot:spring-boot-devtools")

}

tasks.withType<Test> {
    useJUnitPlatform()
}
