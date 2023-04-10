plugins {
    id("boot-web-console.base")
    id("boot-web-console.publish")
    id("java-library")
}

dependencies {
    implementation(project(":boot-web-console-api"))
    compileOnly(libs.boot.web)
}

