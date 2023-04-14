plugins {
    java
    id("jweb-console.base")
    id("io.micronaut.application") version "3.7.7"
}

dependencies {
    annotationProcessor("io.micronaut:micronaut-http-validation")
    implementation("io.micronaut:micronaut-jackson-databind")
    implementation("jakarta.annotation:jakarta.annotation-api")
    runtimeOnly("ch.qos.logback:logback-classic")
    implementation("io.micronaut:micronaut-validation")

    implementation(project(":jweb-console-micronaut-starter"))
    implementation(project(":jweb-console-groovy"))
    implementation(project(":jweb-console-jruby"))
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