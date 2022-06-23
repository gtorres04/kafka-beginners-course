plugins {
    id("java")
}

group = "io.conduktor.demos"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.kafka:kafka-clients:3.1.0")
    implementation("org.slf4j:slf4j-api:1.7.36")
    implementation("org.slf4j:slf4j-simple:1.7.36")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("com.launchdarkly:okhttp-eventsource:2.5.0")

}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}