plugins {
    id("jweb-console.publish")
}

dependencies {
    implementation(project(":jweb-console-api"))
    api(libs.groovy.jsr223)
}
