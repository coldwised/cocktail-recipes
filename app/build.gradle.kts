plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    kotlin("plugin.serialization")
}

android {
    namespace = "com.cocktailbar"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.cocktailbar"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        freeCompilerArgs += listOf(
                "-P",
                "plugin:androidx.compose.compiler.plugins.kotlin:stabilityConfigurationPath=" +
                        project.rootDir + "/compose_compiler_config.conf"
        )
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Decompose
    implementation("com.arkivanov.decompose:decompose:3.0.0-alpha09")
    implementation("com.arkivanov.decompose:extensions-compose:3.0.0-alpha09")

    // Coil
    implementation("io.coil-kt:coil-compose:2.4.0")

    // kotlin-inject
    ksp("me.tatarka.inject:kotlin-inject-compiler-ksp:0.6.3")
    implementation("me.tatarka.inject:kotlin-inject-runtime:0.6.3")

    // Room
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    ksp("androidx.room:room-compiler:$room_version")

    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.compose.ui:ui:1.7.0-alpha05")
    implementation("androidx.compose.ui:ui-graphics:1.7.0-alpha05")
    implementation("androidx.compose.ui:ui-tooling-preview:1.7.0-alpha05")
    implementation("androidx.compose.material3:material3:1.2.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.7.0-alpha05")
    debugImplementation("androidx.compose.ui:ui-tooling:1.7.0-alpha05")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.7.0-alpha05")
}