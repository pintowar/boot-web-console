plugins {
    id("jweb-console.base")
    `maven-publish`
}

publishing {
    repositories {
        maven {
            name = "Sonatype"
            val releasesRepoUrl = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            val snapshotsRepoUrl = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
            setUrl(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)
            credentials {
                username = project.findProperty("ossrh.user")?.toString() ?: System.getenv("SONATYPE_USER")
                password = project.findProperty("ossrh.pass")?.toString() ?: System.getenv("SONATYPE_PASS")
            }
        }
    }
    publications {
        register<MavenPublication>("maven") {
            from(components["java"])
            artifact(tasks["sourcesJar"])
            artifact(tasks["javadocJar"])
        }
    }
}