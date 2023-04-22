plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://plugins.gradle.org/m2/")
    }
}

dependencies {
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
    implementation(libs.gradleplugin.boot)
    implementation(libs.gradleplugin.micronaut)

    implementation(libs.gradleplugin.spotless)
    implementation(libs.gradleplugin.sonarqube)
    implementation(libs.gradleplugin.release)
    implementation(libs.gradleplugin.node)
}