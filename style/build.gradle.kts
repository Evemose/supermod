plugins {
    id("java")
}

group = "com.supermod"
version = "1.0-SNAPSHOT"

java.toolchain.languageVersion = JavaLanguageVersion.of(21)

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("com.puppycrawl.tools:checkstyle:10.17.0")
}

tasks.test {
    useJUnitPlatform()
}