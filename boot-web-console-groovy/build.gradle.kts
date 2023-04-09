plugins {
    id("boot-web-console.base")
    id("boot-web-console.publish")
}

dependencies {
    implementation(project(":boot-web-console-core"))
    api(libs.groovy.jsr223)

    testImplementation(libs.bundles.tests)
}

tasks.withType<Test> {
    useJUnitPlatform()
}
