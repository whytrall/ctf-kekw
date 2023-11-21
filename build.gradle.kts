import java.net.URI

plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
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
        uploadFile(layout.buildDirectory.dir("libs").get().file("untitled5-1.0-SNAPSHOT.jar").asFile, URI("https://task1.jbctf.com/upload_plugin"))
    }
}

tasks.register("buildAndUpload") {
    group = "niiiiice"
    dependsOn(tasks.getByName("jar"))
    outputs.upToDateWhen { false }
    doLast {
        uploadFile(layout.buildDirectory.dir("libs").get().file("untitled5-1.0-SNAPSHOT.jar").asFile, URI("https://task1.jbctf.com/upload_plugin"))
    }
}