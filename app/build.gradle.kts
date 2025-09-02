import java.io.File
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.app"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    // Signing config only if all necessary secrets exist
    val signingKeyPath = System.getenv("SIGNING_KEY_PATH")
    val signingKeyStorePassword = System.getenv("KEYSTORE_PASS")
    val signingKeyAlias = System.getenv("KEY_ALIAS")
    val signingKeyPassword = System.getenv("KEYSTORE_PASSWORD")

    val hasSigning = listOf(signingKeyPath, signingKeyStorePassword, signingKeyAlias, signingKeyPassword)
        .all { !it.isNullOrBlank() }

    signingConfigs {
        if (hasSigning) {
            create("release") {
                storeFile = File(signingKeyPath!!)
                storePassword = signingKeyStorePassword!!
                keyAlias = signingKeyAlias!!
                keyPassword = signingKeyPassword!!
            }
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true

            if (hasSigning) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
        getByName("debug") {
            isDebuggable = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
}
