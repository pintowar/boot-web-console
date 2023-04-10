plugins {
    java
    id("boot-web-console.base")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

dependencies {
    implementation(libs.boot.actuator)
    implementation(libs.boot.web)
    implementation(project(":boot-web-console-boot"))
    implementation(project(":boot-web-console-groovy"))
}