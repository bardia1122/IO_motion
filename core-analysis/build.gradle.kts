plugins {
    alias(libs.plugins.kotlin.jvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}




dependencies {
    implementation(project(":core-common"))
    testImplementation(libs.junit)
}
