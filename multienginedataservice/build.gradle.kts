val kotlinVersion: String by project
val spekVersion: String by project
val graalVersion: String by project
val camundaVersion: String by project

plugins {
    id("com.github.johnrengelman.shadow")
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.kotlin.kapt")
    id("org.jetbrains.kotlin.plugin.allopen")
}

dependencies {

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")

    compileOnly(platform("org.camunda.bpm:camunda-bom:$camundaVersion"))
    compileOnly("org.camunda.bpm:camunda-engine")

    compileOnly("org.camunda.bpm:camunda-engine-plugin-spin")
    compileOnly("org.camunda.spin:camunda-spin-dataformat-json-jackson")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.10.3")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.10.3")

    api("org.litote.kmongo:kmongo:4.2.3")
    kapt("org.litote.kmongo:kmongo-annotation-processor:4.2.3")

    implementation("org.graalvm.sdk:graal-sdk:${graalVersion}")
    implementation("org.graalvm.js:js:${graalVersion}")

}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
}