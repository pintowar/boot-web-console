plugins {
    java
    id("io.micronaut.application")
}

repositories {
    mavenCentral()
}

dependencies {
    annotationProcessor("io.micronaut:micronaut-http-validation")
    implementation("io.micronaut:micronaut-jackson-databind")
    implementation("jakarta.annotation:jakarta.annotation-api")
    runtimeOnly("ch.qos.logback:logback-classic")
    implementation("io.micronaut:micronaut-validation")

    runtimeOnly(project(":jweb-console-micronaut-starter"))
    runtimeOnly(project(":jweb-console-groovy"))
    runtimeOnly(project(":jweb-console-jruby"))
}

graalvmNative.toolchainDetection.set(false)
micronaut {
    runtime("netty")
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("io.github.pintowar.console.*")
    }
}