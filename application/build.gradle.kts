plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
    id("org.springframework.boot")
}

dependencies {
    implementation(project(":api"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.postgresql:postgresql")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${property("webmvcUiVersion")}")
    implementation("org.hibernate.orm:hibernate-envers")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

val copyOpenApiSpec by tasks.registering(Copy::class) {
    group = "documentation"
    description = "Copies the OpenAPI spec from the API module to the Spring Boot resources folder."

    from(file("$projectDir/../api/src/main/resources/user-api.yaml"))
    into(file("src/main/resources/static/api-docs"))
}

tasks.named("processResources") {
    dependsOn("copyOpenApiSpec")
}
