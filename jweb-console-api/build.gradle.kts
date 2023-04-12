plugins {
    id("jweb-console.publish")
    id("java-library")
}

tasks.processResources {
    val webCli = ":jweb-console-webcli"
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
