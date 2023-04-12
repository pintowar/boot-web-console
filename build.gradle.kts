import net.researchgate.release.ReleaseExtension

plugins {
    id("jweb-console.base")
    id("net.researchgate.release")
    id("org.sonarqube")
}

allprojects {
    group = "io.github.pintowar"
    description = "jweb-console"
}

tasks {
    register<JacocoReport>("codeCoverageReport") {
        group = "verification"
        description = "Run tests and merge all jacoco reports"

        val codeCoverageTask = this
        // If a subproject applies the 'jacoco' plugin, add the result it to the report
        subprojects {
            val subproject = this
            subproject.plugins.withType<JacocoPlugin>().configureEach {
                val extensions = subproject.tasks.matching {
                    val hasJacoco = it.extensions.findByType<JacocoTaskExtension>() != null
                    hasJacoco && !it.name.contains("native", ignoreCase = true)
                }

                extensions.forEach { codeCoverageTask.dependsOn(it) }

                extensions.configureEach {
                    val testTask = this
                    sourceSets(subproject.sourceSets.main.get())
                    executionData(testTask)
                }
            }
        }

        reports {
            xml.required.set(true)
            html.required.set(true)
            csv.required.set(true)
        }
    }
}

sonarqube {
    properties {
        val jacocoReportPath = "${project.buildDir.absolutePath}/reports/jacoco/codeCoverageReport"
        val sonarToken = project.findProperty("sonar.token")?.toString() ?: System.getenv("SONAR_TOKEN")
        property("sonar.sourceEncoding", "UTF-8")
        property("sonar.organization", "pintowar")
        property("sonar.projectName", "jweb-console")
        property("sonar.projectKey", "pintowar_jweb-console")
        property("sonar.projectVersion", project.version.toString())
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.login", sonarToken)
        property("sonar.verbose", true)
        property("sonar.github.repository", "pintowar/jweb-console")
        property("sonar.coverage.jacoco.xmlReportPaths", "$jacocoReportPath/codeCoverageReport.xml")
        property("sonar.exclusions", "**/sample/*.java")
    }
}

configure<ReleaseExtension> {
    tagTemplate.set("v\$version")
    with(git) {
        requireBranch.set("master")
    }
}

tasks.afterReleaseBuild {
    dependsOn(":jweb-console-api:publish")
}