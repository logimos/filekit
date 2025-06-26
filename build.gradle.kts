plugins {
    kotlin("jvm") version "2.0.0"
}

group = "com.logimos.filekit"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
