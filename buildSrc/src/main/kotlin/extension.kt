import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.the

val Project.libs: LibrariesForLibs
    get() = the<LibrariesForLibs>()

fun Project.allLangSubModules() = this.rootProject
        .subprojects
        .filter { it.name.startsWith("jweb-console-lang-") }
