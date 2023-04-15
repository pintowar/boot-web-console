plugins {
    id("jweb-console.publish")
    id("io.micronaut.library")
    id("java-library")
}

dependencies {
    api(project(":jweb-console-api"))
    allLangSubModules().forEach(::runtimeOnly)

    implementation(libs.micronaut.router)
    annotationProcessor(libs.micronaut.validation)
}

micronaut {
    version.set("3.5.1")
}