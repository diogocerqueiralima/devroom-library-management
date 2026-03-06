plugins {
    id("java")
    id("com.gradleup.shadow") version "9.3.2"
}

group = "com.github.diogocerqueiralima"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.commons:commons-csv:1.14.1")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.jar {
    manifest {
        attributes(
            "Main-Class" to "com.github.diogocerqueiralima.LibraryManagementApplication"
        )
    }
}

tasks.test {
    useJUnitPlatform()
}