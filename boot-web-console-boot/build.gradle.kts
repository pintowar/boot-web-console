plugins {
    id("boot-web-console.base")
    id("boot-web-console.publish")
    id("io.spring.dependency-management")
    id("java-library")
}

dependencyManagement {
    imports {
        mavenBom(libs.boot.dependencies.get().toString())
    }
}

dependencies {
    api(project(":boot-web-console-api"))
    implementation(libs.boot.web)

    annotationProcessor(libs.bundles.boot.config.processors)
}

