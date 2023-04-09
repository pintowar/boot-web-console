plugins {
    id("boot-web-console.base")
    id("boot-web-console.publish")
    id("java-library")
}

dependencies {
    compileOnly(libs.boot.web)
}

tasks.processResources {
    val webCli = ":boot-web-console-webcli"
    dependsOn("$webCli:build")

    doLast {
        val origin = project(webCli).buildDir.absolutePath
        val dest = "${project.buildDir.absolutePath}/resources/main/public/console"
        copy {
            from(origin)
            into(dest)
        }
        logger.quiet("Cli Resources: move from $origin to $dest")
    }
}
