plugins {
    java
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.boot.actuator)
    implementation(libs.boot.web)

    developmentOnly(project(":jweb-console-spring-boot-starter"))
    developmentOnly(project(":jweb-console-groovy"))
    developmentOnly(project(":jweb-console-jruby"))
}