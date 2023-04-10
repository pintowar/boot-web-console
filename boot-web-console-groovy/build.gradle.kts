plugins {
    id("boot-web-console.base")
    id("boot-web-console.publish")
}

dependencies {
    implementation(project(":boot-web-console-api"))
    api(libs.groovy.jsr223)
}
