import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
	id("fabric-loom")
	kotlin("jvm")
	`maven-publish`
}

version = property("mod_version")!!
group = property("maven_group")!!

repositories {
	mavenCentral()
	maven("https://jitpack.io")
	maven("https://maven.meteordev.org/releases")
	maven("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1")
	maven("https://maven.terraformersmc.com/")
}

dependencies {
	// To change the versions see the gradle.properties file
	minecraft("com.mojang:minecraft:${property("minecraft_version")}")
	mappings("net.fabricmc:yarn:${property("yarn_mappings")}:v2")
	modImplementation("net.fabricmc:fabric-loader:${property("loader_version")}")
	modImplementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_version")}")
	modImplementation("net.fabricmc:fabric-language-kotlin:${property("fabric_kotlin_version")}")

	property("orbit_version").let {
		modImplementation("meteordevelopment:orbit:$it")
		include("meteordevelopment:orbit:$it")
	}

	property("commodore_version").let {
		implementation("com.github.stivais:Commodore:$it")
		include("com.github.stivais:Commodore:$it")
	}

	property("okhttp_version").let {
		implementation("com.squareup.okhttp3:okhttp:$it")
		include("com.squareup.okhttp3:okhttp:$it")
	}

	property("okio_version").let {
		implementation("com.squareup.okio:okio:$it")
		include("com.squareup.okio:okio:${it}")	}
}

tasks {
	processResources {
		filesMatching("fabric.mod.json") {
			expand(getProperties())
		}
	}

	compileKotlin {
		compilerOptions {
			jvmTarget = JvmTarget.JVM_21
		}
	}
}

java {
	withSourcesJar()
}