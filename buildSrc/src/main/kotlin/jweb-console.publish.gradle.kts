plugins {
    id("jweb-console.base")
    `maven-publish`
    signing
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

            pom {
                name.set(project.name.trim().split("-").joinToString(" ") {
                    it.replaceFirstChar(Char::uppercase)
                })
                description.set(project.description)
                url.set("https://github.com/pintowar/jweb-console")
                licenses {
                    license {
                        name.set("The MIT License (MIT)")
                        url.set("https://mit-license.org/license.txt")
                    }
                }
                developers {
                    developer {
                        id.set("pintowar")
                        name.set("Thiago Oliveira")
                        email.set("pintowar@gmail.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/pintowar/jweb-console.git")
                    developerConnection.set("scm:git:ssh://github.com/pintowar/jweb-console.git")
                    url.set("https://github.com/pintowar/jweb-console/")
                }
            }
        }
    }
}

signing {
    val signingKeyId = project.findProperty("signing.keyId")?.toString() ?: System.getenv("OSSRH_KEYID")
    val signingKey = project.findProperty("signing.secretKey")?.toString() ?: System.getenv("OSSRH_SECRET_KEY")
    val signingPassword = project.findProperty("signing.password")?.toString() ?: System.getenv("OSSRH_PASSWORD")
    useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
    sign(publishing.publications["maven"])
}
