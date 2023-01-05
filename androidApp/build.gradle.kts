
plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    kotlin("plugin.serialization") version Dependencies.kotlinVersion
}

android {
    compileSdk = 33
    defaultConfig {
        applicationId = "com.rhea.translator.android"
        minSdk = 23
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Dependencies.composeVersion
    }
}

dependencies {
    implementation(project(":shared"))
    implementation(Dependencies.composeUi)
    implementation(Dependencies.composeUiTooling)
    implementation(Dependencies.composeUiToolingPreview)
    implementation(Dependencies.composeFoundation)
    implementation(Dependencies.composeMaterial)
    implementation(Dependencies.activityCompose)
    implementation(Dependencies.composeIconsExtended)
    implementation(Dependencies.composeNavigation)
    implementation(Dependencies.coilCompose)

    implementation(Dependencies.hiltAndroid)
    kapt(Dependencies.hiltAndroidCompiler)
    kapt(Dependencies.hiltCompiler)
    implementation(Dependencies.hiltNavigationCompose)

    implementation(Dependencies.ktorAndroid)

    androidTestImplementation(Dependencies.testRunner)
    androidTestImplementation(Dependencies.jUnit)
    androidTestImplementation(Dependencies.composeTesting)
    debugImplementation(Dependencies.composeTestManifest)
}