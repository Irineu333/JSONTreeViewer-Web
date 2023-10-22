plugins {
    kotlin("multiplatform") version "1.9.0"
    id("org.jetbrains.compose") version "1.5.3"
}

group = "org.example"
version = "1.0-DEV"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(19))
        vendor.set(JvmVendorSpec.ORACLE)
    }
}

kotlin {

    js {

        binaries.executable()

        browser()
    }

    sourceSets {

        val jsMain by getting {
            dependencies {
                implementation(compose.material3)
                implementation("cafe.adriel.voyager:voyager-navigator:1.0.0-rc05")
            }
        }
    }
}

compose.experimental {
    web.application {}
}