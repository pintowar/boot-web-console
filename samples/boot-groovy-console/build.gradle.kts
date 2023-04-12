plugins {
    java
    id("jweb-console.base")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

dependencies {
    implementation(libs.boot.actuator)
    implementation(libs.boot.web)

    developmentOnly(project(":jweb-console-boot"))
    developmentOnly(project(":jweb-console-groovy"))
}