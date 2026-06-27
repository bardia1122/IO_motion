plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}



dependencies {
    implementation(project(":core-common"))
    implementation(project(":core-analysis"))
    implementation(libs.kotlinx.serialization.json)
    testImplementation(libs.junit)
}
