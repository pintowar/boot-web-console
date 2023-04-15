plugins {
    `java-library`
    id("com.diffplug.spotless")
    id("jacoco")
    id("idea")
}

repositories {
    mavenLocal()
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

dependencies {
    testImplementation(libs.bundles.tests)
}

spotless {
    java {
        googleJavaFormat()
        removeUnusedImports()
        indentWithSpaces()
        importOrder()
        formatAnnotations()
        endWithNewline()
    }
//    format("misc") {
//        target("**/.gitignore", "**/*.gradle", "README.md")
//        indentWithSpaces()
//        trimTrailingWhitespace()
//        endWithNewline()
//    }
}

tasks {
    register<Jar>("sourcesJar") {
        archiveClassifier.set("sources")
        archiveExtension.set("jar")
        from(sourceSets["main"].allSource)
    }

    register<Jar>("javadocJar") {
        archiveClassifier.set("javadoc")
        archiveExtension.set("jar")
    }

    test {
        useJUnitPlatform()
        finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
    }

    jacocoTestReport {
        dependsOn(tasks.test) // tests are required to run before generating the report
    }
}