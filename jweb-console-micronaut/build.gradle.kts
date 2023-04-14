plugins {
    id("jweb-console.publish")
    id("io.spring.dependency-management")
    id("java-library")
}

dependencies {
    api(project(":jweb-console-api"))
    implementation("io.micronaut:micronaut-router:3.8.8")

    annotationProcessor("io.micronaut:micronaut-http-validation:3.8.8")

//    annotationProcessor(libs.bundles.boot.config.processors)
}
