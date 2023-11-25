import java.net.URI

plugins {
    id("java")
    kotlin("jvm")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("com.amazonaws:aws-java-sdk-s3:1.12.595")
    implementation("com.amazonaws:aws-java-sdk-core:1.9.17")
    implementation(kotlin("stdlib-jdk8"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    manifest {
        attributes(mapOf("Main-Class" to "org.example.Main"))
    }
}

tasks.register("upload") {
    group = "niiiiice"
    outputs.upToDateWhen { false }
    doLast {
        uploadFile(layout.buildDirectory.dir("libs").get().file("untitled5-1.0-SNAPSHOT.jar").asFile, URI("https://task2.jbctf.com/upload_plugin"))
    }
}

tasks.register("buildAndUpload") {
    group = "niiiiice"
    dependsOn(tasks.getByName("jar"))
    outputs.upToDateWhen { false }
    doLast {
        uploadFile(layout.buildDirectory.dir("libs").get().file("untitled5-1.0-SNAPSHOT.jar").asFile, URI("https://task2.jbctf.com/upload_plugin"))
    }
}
kotlin {
    jvmToolchain(17)
}