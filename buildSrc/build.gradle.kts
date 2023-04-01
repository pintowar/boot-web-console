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
    implementation(libs.gradleplugin.boot)

    implementation(libs.gradleplugin.sonarqube)
    implementation(libs.gradleplugin.release)
}