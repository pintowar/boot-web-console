plugins {
    id("boot-web-console.base")
    id("java-library")
}

dependencies {
    compileOnly(libs.boot.web)
    implementation(libs.groovy)

    testImplementation(libs.boot.web)
    testImplementation(libs.boot.test) {
        exclude(module = "commons-logging")
    }
    testImplementation(libs.jgiven.junit)
    testImplementation(libs.jgiven.spring)
}
