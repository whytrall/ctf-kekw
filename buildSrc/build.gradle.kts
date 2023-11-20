plugins {
  kotlin("jvm") version "1.8.0"
}

repositories {
  maven { setUrl("https://cache-redirector.jetbrains.com/repo1.maven.org/maven2") }
  maven { setUrl("https://cache-redirector.jetbrains.com/download.jetbrains.com/teamcity-repository") }
}

dependencies {
  implementation("org.apache.commons:commons-compress:1.20")
  implementation("com.google.code.gson:gson:2.9.1")
  implementation("com.google.guava:guava:28.0-jre")
  implementation("org.jetbrains.teamcity:serviceMessages:2019.1.4")
  implementation("org.apache.httpcomponents:httpclient:4.5.13")
  implementation("org.apache.httpcomponents:httpmime:4.5.13")
  api("org.apache.commons:commons-text:1.10.0")
}

kotlin {
  jvmToolchain(17)
}