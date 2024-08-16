plugins {
    java
}

group = "dev.xdark"
version = "1.0"

repositories {
    mavenCentral()
}

tasks.withType<JavaCompile>().configureEach {
    sourceCompatibility = JavaVersion.VERSION_1_8.toString()
    targetCompatibility = sourceCompatibility
}
val latestVersion = JavaLanguageVersion.of(22)
java.toolchain.languageVersion.set(latestVersion)
tasks.withType<JavaExec>().configureEach {
    executable(javaToolchains.launcherFor {
        languageVersion.set(latestVersion)
    }.get().executablePath)
}
