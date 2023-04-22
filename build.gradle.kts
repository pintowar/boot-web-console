import net.researchgate.release.ReleaseExtension

plugins {
    base
    id("idea")
    id("jacoco-report-aggregation")
    id("net.researchgate.release")
    id("org.sonarqube")
    id("com.diffplug.spotless")
}

allprojects {
    group = "io.github.pintowar"
    description = "jweb-console"
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    allStarterSubModules.forEach(::jacocoAggregation)
}

reporting {
    reports {
        val testCodeCoverageReport by creating(JacocoCoverageReport::class) {
            testType.set(TestSuiteType.UNIT_TEST)
        }
    }
}

tasks.check {
    dependsOn(tasks.named<JacocoReport>("testCodeCoverageReport"))
}

spotless {
    format("misc") {
        target("**/.gitignore", "**/*.gradle", "README.md")
        indentWithSpaces()
        trimTrailingWhitespace()
        endWithNewline()
    }
}

sonarqube {
    properties {
        val sonarToken = project.findProperty("sonar.token")?.toString() ?: System.getenv("SONAR_TOKEN")
        val jacocoReportPath = "${project.buildDir.absolutePath}/reports/jacoco/testCodeCoverageReport"
        val lcovReportPath = "${project("jweb-console-webcli").projectDir.absolutePath}/coverage/"

        property("sonar.sourceEncoding", "UTF-8")
        property("sonar.organization", "pintowar")
        property("sonar.projectName", "jweb-console")
        property("sonar.projectKey", "pintowar_jweb-console")
        property("sonar.projectVersion", project.version.toString())
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.login", sonarToken)
        property("sonar.verbose", true)
        property("sonar.github.repository", "pintowar/jweb-console")
        property("sonar.exclusions", "*/src/test/**,**/sample/*.java")
        property("sonar.coverage.jacoco.xmlReportPaths", "$jacocoReportPath/testCodeCoverageReport.xml")
        property("sonar.javascript.lcov.reportPaths", "$lcovReportPath/lcov.info")
    }
}

configure<ReleaseExtension> {
    tagTemplate.set("v\$version")
    with(git) {
        requireBranch.set("master")
    }
}

tasks.sonar {
    dependsOn(":testCodeCoverageReport")
}

tasks.afterReleaseBuild {
//    dependsOn(":sonar", ":publish")
    dependsOn(":sonar")
}
