plugins {
    kotlin("jvm")
    id("org.openapi.generator")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${property("webmvcUiVersion")}")
    implementation("org.openapitools:jackson-databind-nullable:${property("jacksonDatabindNullableVersion")}")
}

openApiGenerate {
    generatorName.set("kotlin-spring")
    inputSpec.set("$projectDir/src/main/resources/user-api.yaml")
    outputDir.set("$buildDir/generated")
    apiPackage.set("dev.audit.demo.api")
    modelPackage.set("dev.audit.demo.model")
    validateSpec.set(true)
    configOptions.set(
        mapOf(
            "library" to "spring-boot",
            "interfaceOnly" to "true",
            "useSpringBoot3" to "true",
            "apiNameSuffix" to "Api",
        )
    )
}

tasks.named("compileKotlin") {
    dependsOn("openApiGenerate")
}

sourceSets["main"].java.srcDir("$buildDir/generated/src/main/kotlin")