plugins {
    id("jweb-console.publish")
    id("io.spring.dependency-management")
    id("java-library")
}

dependencyManagement {
    imports {
        mavenBom(libs.boot.dependencies.get().toString())
    }
}

dependencies {
    api(project(":jweb-console-api"))
    implementation(libs.boot.web)

    annotationProcessor(libs.bundles.boot.config.processors)
}

