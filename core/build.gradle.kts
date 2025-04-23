import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "ru.gilgamesh.abon.core"
    compileSdk = 35

    defaultConfig {
        minSdk = 29

        //testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    val properties = gradleLocalProperties(rootDir, providers)

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField(
                "String", "HOST_MICRO_URL", properties.getProperty("api.host.dev")
            )
        }
        debug {
            isMinifyEnabled = false
            buildConfigField(
                "String", "HOST_MICRO_URL", properties.getProperty("api.host.dev")
            )
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
    api("androidx.core:core-ktx:1.16.0")
    api("androidx.appcompat:appcompat:1.7.0")
    api("com.google.android.material:material:1.12.0")
    //glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
    ksp("com.github.bumptech.glide:ksp:4.16.0")
    //di
    api("com.google.dagger:hilt-android:2.56.2")
    ksp("com.google.dagger:hilt-compiler:2.56.2")
    api("androidx.hilt:hilt-navigation-compose:1.2.0")

    //network
    api("com.squareup.retrofit2:retrofit:2.11.0")
    api("com.squareup.retrofit2:converter-gson:2.11.0")
    api("com.squareup.okhttp3:okhttp:4.12.0")
    api("com.squareup.okhttp3:logging-interceptor:4.12.0")
    // Room
    api("androidx.room:room-runtime:2.7.0")
    ksp("androidx.room:room-compiler:2.7.0")
    api("androidx.room:room-ktx:2.7.0")

    api("commons-io:commons-io:2.19.0")
    //ui
    api("androidx.constraintlayout:constraintlayout:2.2.1")
    api("androidx.lifecycle:lifecycle-livedata-ktx:2.8.7")
    api("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
    api("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    api("androidx.navigation:navigation-fragment-ktx:2.8.9")
    api("androidx.navigation:navigation-ui-ktx:2.8.9")
    api("androidx.activity:activity-ktx:1.10.1")
    api("androidx.fragment:fragment-ktx:1.8.6")
    api("androidx.recyclerview:recyclerview:1.4.0")
}