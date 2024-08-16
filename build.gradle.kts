plugins {
    java
    `maven-publish`
}

group = "dev.xdark"
version = "1.0"

repositories {
    mavenCentral()
}

tasks.withType<JavaCompile>().configureEach {
    sourceCompatibility = JavaVersion.VERSION_1_9.toString()
    targetCompatibility = sourceCompatibility
}
val latestVersion = JavaLanguageVersion.of(22)
java.toolchain.languageVersion.set(latestVersion)
java.withSourcesJar()
tasks.withType<JavaExec>().configureEach {
    executable(javaToolchains.launcherFor {
        languageVersion.set(latestVersion)
    }.get().executablePath)
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}
