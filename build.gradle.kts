import net.researchgate.release.ReleaseExtension

plugins {
    id("boot-web-console.base")
    id("net.researchgate.release")
    id("org.sonarqube")
}

allprojects {
    group = "io.github.pintowar"
    description = "boot-web-console"
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
        property("sonar.projectName", "boot-web-console")
        property("sonar.projectKey", "pintowar_boot-web-console")
        property("sonar.projectVersion", project.version.toString())
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.login", sonarToken)
        property("sonar.verbose", true)
        property("sonar.github.repository", "pintowar/boot-web-console")
        property("sonar.coverage.jacoco.xmlReportPaths", "$jacocoReportPath/codeCoverageReport.xml")
    }
}

configure<ReleaseExtension> {
    tagTemplate.set("v\$version")
    with(git) {
        requireBranch.set("master")
    }
}

tasks.afterReleaseBuild {
    dependsOn(":boot-web-console-core:publish")
}