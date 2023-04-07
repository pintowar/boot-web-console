plugins {
    id("boot-web-console.base")
    id("boot-web-console.publish")
}

dependencies {
    implementation(project(":boot-web-console-core"))
    api(libs.groovy)
    api(libs.groovy.jsr223)

    testImplementation(libs.boot.web)
    testImplementation(libs.boot.test)
}

tasks.withType<Test> {
    useJUnitPlatform()
}
