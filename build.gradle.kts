import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    java
    kotlin("jvm") version "2.2.0"
    kotlin("plugin.lombok") version "2.1.20"
    id("io.freefair.lombok") version "8.10.2"
    id("com.gradleup.shadow") version "8.3.0"
}

group = "cn.irina"
version = "0.1"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") {
        name = "spigotmc-repo"
    }
    maven("https://oss.sonatype.org/content/groups/public/") {
        name = "sonatype"
    }

    maven("https://repo.panda-lang.org/releases") {
        name = "panda-repo"
    }
    maven("https://jitpack.io")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.codemc.io/repository/maven-releases/")
    maven("https://repo.codemc.io/repository/maven-snapshots/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20.4-R0.1-SNAPSHOT")

    implementation("org.jetbrains:annotations:26.0.2")

    implementation("io.github.revxrsal:lamp.common:4.0.0-rc.12")
    implementation("io.github.revxrsal:lamp.bukkit:4.0.0-rc.12")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.2.0")
    implementation("org.reflections:reflections:0.10.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")

    compileOnly(fileTree(baseDir = "libs"))
}


val targetJavaVersion = 21
kotlin {
    jvmToolchain(targetJavaVersion)
    compilerOptions {
        javaParameters = true
    }
}

tasks.compileJava {
    options.compilerArgs.add("-parameters")
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.compilerArgs.add("-nowarn")
    }

    shadowJar {
        exclude("**/*.kotlin_metadata")
        exclude("**/*.kotlin_module")
        exclude("**/*.kotlin_builtins")
        exclude("io/netty/**")

        minimize()

        relocate("org.reflections", "cn.irina.thirdparty.reflections")
        relocate("org.jetbrains", "cn.irina.thirdparty.jetbrains")
        relocate("org.slf4j", "cn.irina.thirdparty.slf4j")
        relocate("kotlin", "cn.irina.thirdparty.kotlin")
        relocate("javax", "cn.irina.thirdparty.javax")
        relocate("javassist", "cn.irina.thirdparty.javassist")

        relocate("revxrsal", "cn.irina.thirdparty.revxrsal")

        destinationDirectory = file("$projectDir/jar")

        archiveFileName.set("IrinaGameCore.jar")
    }

    jar {
        enabled = false
    }

    build {
        dependsOn("shadowJar")
    }
}
