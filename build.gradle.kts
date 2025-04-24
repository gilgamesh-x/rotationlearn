import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    // Add the dependency for the Crashlytics Gradle plugin
    id("com.google.firebase.crashlytics")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.0"
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
    //id("kotlin-kapt")
    id("xyz.mishkun.lobzik") version "0.7.0"
}

lobzik {
    // regex for your app build variants
    variantName.set("debug")
    // path of your monolith module for report
    monolithModule.set(":app")
    // paths of your feature modules (you can use regex here)
    featureModulesRegex.add(":feature:.*")
    // package prefix for your modules to filter only your own code
    // and exclude code of deps like java.util.*, androidx.*, etc.
    packagePrefix.set("ru.gilgamesh.abon.motot")
    // list of regexes of ignored classes. Add your glue code patterns here
    //ignoredClasses.addAll(".*Command$", ".*Coordinator$",  "^MyApplication$")
}

android {
    val properties = gradleLocalProperties(rootDir, providers)
    signingConfigs {
        create("release") {
            storeFile = file(properties.getProperty("mh.storeFile"))
            storePassword = properties.getProperty("mh.storePassword")
            keyAlias = properties.getProperty("mh.keyAlias")
            keyPassword = properties.getProperty("mh.keyPassword")
        }
    }
    namespace = "ru.gilgamesh.abon.motot"
    compileSdk = 35

    lint {
        checkReleaseBuilds = false
        // Or, if you prefer, you can continue to check for errors in release builds,
        // but continue the build even when errors are found:
        abortOnError = false
    }/*fun Packaging.() {
        jniLibs {
            useLegacyPackaging = true
        }
    }*/
    defaultConfig {
        applicationId = "ru.gilgamesh.abon.motot"
        minSdk = 29
        targetSdk = 35
        versionCode = 105
        versionName = "3.3.0"

        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "HOST_MICRO_URL", properties.getProperty("api.host.dev"))
        buildConfigField(
            "String", "HOST_MAP_URL", properties.getProperty("api.web.map.url.dev")
        )
        buildConfigField(
            "String", "HOST_WSS_URL", properties.getProperty("api.wss.dev")
        )
        buildConfigField(
            "String", "HOST_MAP_ROUTE_URL", properties.getProperty("api.web.maproute.url.dev")
        )
        buildConfigField(
            "String",
            "HOST_MAP_GENERATE_ROUTE_URL",
            properties.getProperty("api.web.maproutegen.url.dev")
        )
        buildConfigField(
            "String",
            "HOST_MAP_TRACK_ROUTE_URL",
            properties.getProperty("api.web.maproutetrack.url.dev")
        )
        buildConfigField(
            "String", "ADS_ROUTE_ALL", properties.getProperty("ads.yandex.demo.banner")
        )
        buildConfigField(
            "String", "ADS_ROUTE_FAVORITE", properties.getProperty("ads.yandex.demo.banner")
        )
        buildConfigField("String", "ADS_ROUTE_MY", properties.getProperty("ads.yandex.demo.banner"))
        buildConfigField("String", "ADS_CHAT", properties.getProperty("ads.yandex.demo.banner"))
        buildConfigField(
            "String", "ADS_PAIR", properties.getProperty("ads.yandex.demo.interstitial")
        )
        buildConfigField(
            "String", "ADS_COMPETITION_ALL", properties.getProperty("ads.yandex.demo.banner")
        )
        buildConfigField(
            "String", "ADS_COMPETITION_MEMBER", properties.getProperty("ads.yandex.demo.banner")
        )
        buildConfigField(
            "String", "ADS_COMPETITION_CREATED", properties.getProperty("ads.yandex.demo.banner")
        )
        buildConfigField("String", "ADS_GROUP", properties.getProperty("ads.yandex.demo.banner"))
        buildConfigField("String", "WEATHER_TOKEN", properties.getProperty("api.weather.dev"))
        buildConfigField("String", "YANDEX_SDK_API", properties.getProperty("api.yandex.map.dev"))
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        release {
            buildConfigField(
                "String", "HOST_MICRO_URL", properties.getProperty("api.host.prod")
            )
            buildConfigField(
                "String", "HOST_MAP_URL", properties.getProperty("api.web.map.url.prod")
            )
            buildConfigField(
                "String", "HOST_WSS_URL", properties.getProperty("api.wss.prod")
            )
            buildConfigField(
                "String", "HOST_MAP_ROUTE_URL", properties.getProperty("api.web.maproute.url.prod")
            )
            buildConfigField(
                "String",
                "HOST_MAP_GENERATE_ROUTE_URL",
                properties.getProperty("api.web.maproutegen.url.prod")
            )
            buildConfigField(
                "String",
                "HOST_MAP_TRACK_ROUTE_URL",
                properties.getProperty("api.web.maproutetrack.url.prod")
            )
            buildConfigField(
                "String",
                "ADS_ROUTE_ALL",
                properties.getProperty("ads.yandex.ROUTE_ALL")
            )
            buildConfigField(
                "String",
                "ADS_ROUTE_FAVORITE",
                properties.getProperty("ads.yandex.ROUTE_FAVORITE")
            )
            buildConfigField(
                "String",
                "ADS_ROUTE_MY",
                properties.getProperty("ads.yandex.ROUTE_MY")
            )
            buildConfigField("String", "ADS_CHAT", properties.getProperty("ads.yandex.CHAT"))
            buildConfigField("String", "ADS_PAIR", properties.getProperty("ads.yandex.PAIR"))
            buildConfigField(
                "String",
                "ADS_COMPETITION_ALL",
                properties.getProperty("ads.yandex.COMPETITION_ALL")
            )
            buildConfigField(
                "String",
                "ADS_COMPETITION_MEMBER",
                properties.getProperty("ads.yandex.COMPETITION_MEMBER")
            )
            buildConfigField(
                "String",
                "ADS_COMPETITION_CREATED",
                properties.getProperty("ads.yandex.COMPETITION_CREATED")
            )
            buildConfigField("String", "ADS_GROUP", properties.getProperty("ads.yandex.GROUP"))
            buildConfigField("String", "WEATHER_TOKEN", properties.getProperty("api.weather.prod"))
            buildConfigField(
                "String", "YANDEX_SDK_API", properties.getProperty("api.yandex.map.prod")
            )
            isMinifyEnabled = true
            isDebuggable = false
            isJniDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
            multiDexEnabled = true
        }
        debug {
            buildConfigField(
                "String", "HOST_MICRO_URL", properties.getProperty("api.host.dev")
            )
            buildConfigField(
                "String", "HOST_MAP_URL", properties.getProperty("api.web.map.url.dev")
            )
            buildConfigField(
                "String", "HOST_WSS_URL", properties.getProperty("api.wss.dev")
            )
            buildConfigField(
                "String", "HOST_MAP_ROUTE_URL", properties.getProperty("api.web.maproute.url.dev")
            )
            buildConfigField(
                "String",
                "HOST_MAP_GENERATE_ROUTE_URL",
                properties.getProperty("api.web.maproutegen.url.dev")
            )
            buildConfigField(
                "String",
                "HOST_MAP_TRACK_ROUTE_URL",
                properties.getProperty("api.web.maproutetrack.url.dev")
            )
            buildConfigField(
                "String", "ADS_ROUTE_ALL", properties.getProperty("ads.yandex.demo.banner")
            )
            buildConfigField(
                "String", "ADS_ROUTE_FAVORITE", properties.getProperty("ads.yandex.demo.banner")
            )
            buildConfigField(
                "String", "ADS_ROUTE_MY", properties.getProperty("ads.yandex.demo.banner")
            )
            buildConfigField("String", "ADS_CHAT", properties.getProperty("ads.yandex.demo.banner"))
            buildConfigField(
                "String", "ADS_PAIR", properties.getProperty("ads.yandex.demo.interstitial")
            )
            buildConfigField(
                "String", "ADS_COMPETITION_ALL", properties.getProperty("ads.yandex.demo.banner")
            )
            buildConfigField(
                "String", "ADS_COMPETITION_MEMBER", properties.getProperty("ads.yandex.demo.banner")
            )
            buildConfigField(
                "String",
                "ADS_COMPETITION_CREATED",
                properties.getProperty("ads.yandex.demo.banner")
            )
            buildConfigField(
                "String", "ADS_GROUP", properties.getProperty("ads.yandex.demo.banner")
            )
            buildConfigField("String", "WEATHER_TOKEN", properties.getProperty("api.weather.dev"))
            buildConfigField(
                "String", "YANDEX_SDK_API", properties.getProperty("api.yandex.map.dev")
            )

            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
            multiDexEnabled = true
        }
        create("prod_debug") {
            buildConfigField(
                "String", "HOST_MICRO_URL", properties.getProperty("api.host.prod")
            )
            buildConfigField(
                "String", "HOST_MAP_URL", properties.getProperty("api.web.map.url.prod")
            )
            buildConfigField(
                "String", "HOST_WSS_URL", properties.getProperty("api.wss.prod")
            )
            buildConfigField(
                "String", "HOST_MAP_ROUTE_URL", properties.getProperty("api.web.maproute.url.prod")
            )
            buildConfigField(
                "String",
                "HOST_MAP_GENERATE_ROUTE_URL",
                properties.getProperty("api.web.maproutegen.url.prod")
            )
            buildConfigField(
                "String",
                "HOST_MAP_TRACK_ROUTE_URL",
                properties.getProperty("api.web.maproutetrack.url.prod")
            )
            buildConfigField(
                "String",
                "ADS_ROUTE_ALL",
                properties.getProperty("ads.yandex.ROUTE_ALL")
            )
            buildConfigField(
                "String",
                "ADS_ROUTE_FAVORITE",
                properties.getProperty("ads.yandex.ROUTE_FAVORITE")
            )
            buildConfigField(
                "String",
                "ADS_ROUTE_MY",
                properties.getProperty("ads.yandex.ROUTE_MY")
            )
            buildConfigField("String", "ADS_CHAT", properties.getProperty("ads.yandex.CHAT"))
            buildConfigField("String", "ADS_PAIR", properties.getProperty("ads.yandex.PAIR"))
            buildConfigField(
                "String",
                "ADS_COMPETITION_ALL",
                properties.getProperty("ads.yandex.COMPETITION_ALL")
            )
            buildConfigField(
                "String",
                "ADS_COMPETITION_MEMBER",
                properties.getProperty("ads.yandex.COMPETITION_MEMBER")
            )
            buildConfigField(
                "String",
                "ADS_COMPETITION_CREATED",
                properties.getProperty("ads.yandex.COMPETITION_CREATED")
            )
            buildConfigField("String", "ADS_GROUP", properties.getProperty("ads.yandex.GROUP"))
            buildConfigField("String", "WEATHER_TOKEN", properties.getProperty("api.weather.prod"))
            buildConfigField(
                "String", "YANDEX_SDK_API", properties.getProperty("api.yandex.map.prod")
            )
            isMinifyEnabled = false
            isDebuggable = true
            isJniDebuggable = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
        compileOptions {
            isCoreLibraryDesugaringEnabled = true
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }
        buildFeatures {
            viewBinding = true
        }
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

    implementation("com.android.support:appcompat-v7:28.0.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.9")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.9")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("com.github.dhaval2404:imagepicker:2.1")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    //implementation("commons-io:commons-io:2.4")
    implementation("com.google.android.gms:play-services-location:21.3.0")
    implementation("androidx.activity:activity-ktx:1.10.1")
    implementation("androidx.fragment:fragment-ktx:1.8.6")
    implementation("androidx.core:core-ktx:1.16.0")
    //testImplementation("junit:junit:4.13.2")
    //androidTestImplementation("androidx.test.ext:junit:1.2.1")
    //androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    constraints {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:2.0.0") {
            because("kotlin-stdlib-jdk7 is now a part of kotlin-stdlib")
        }
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:2.0.0") {
            because("kotlin-stdlib-jdk8 is now a part of kotlin-stdlib")
        }
    }
    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.12.0"))
    // TODO: Add the dependencies for Firebase products you want to use
    // When using the BoM, don't specify versions in Firebase dependencies
    // When using the BoM, you don't specify versions in Firebase libr ary dependencies
    implementation("com.google.firebase:firebase-crashlytics")
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-messaging")
    implementation("com.google.android.gms:play-services-base:18.7.0")
    // Add the dependencies for any other desired Firebase products
    // https://firebase.google.com/docs/android/setup#available-libraries

    //implementation("org.java-websocket:Java-WebSocket:1.3.0")
    implementation("io.reactivex.rxjava2:rxjava:2.2.21")
    implementation("com.github.NaikSoftware:StompProtocolAndroid:1.6.6")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("com.yandex.android:mobileads:7.12.0")
    //implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.2.0-alpha01")
    implementation("com.otaliastudios:zoomlayout:1.9.0")
    implementation("io.github.elye:loaderviewlibrary:3.0.0")
    implementation("com.hbb20:ccp:2.7.3")
    // Облегченная библиотека, содержит только карту, слой пробок,
    // LocationManager, UserLocationLayer
    // и возможность скачивать офлайн-карты (только в платной версии).
    //implementation ("com.yandex.android:maps.mobile:4.5.0-lite")

    // Полная библиотека в дополнение к lite версии предоставляет автомобильную маршрутизацию,
    // веломаршрутизацию, пешеходную маршрутизацию и маршрутизацию на общественном транспорте,
    // поиск, suggest, геокодирование и отображение панорам.
    implementation("com.yandex.android:maps.mobile:4.13.0-full")
    implementation("com.android.support:multidex:2.0.1") //enter the latest multidex version
    implementation("com.github.midorikocak:currency-picker-android:1.2.1")
    //implementation("io.appmetrica.analytics:analytics:6.4.0")
    //implementation("io.appmetrica.analytics:push:3.0.0")
    //implementation("androidx.legacy:legacy-support-v4:1.0.0")


    //compose
    implementation("androidx.activity:activity-compose:1.10.1")
    implementation("androidx.compose.runtime:runtime-android:1.7.8")
    implementation(platform("androidx.compose:compose-bom:2025.04.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-util")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.ui:ui-tooling:1.7.8")

    //di
    implementation("com.google.dagger:hilt-android:2.56.2")
    ksp("com.google.dagger:hilt-compiler:2.56.2")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    //network
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")

    //fingerpring device
    implementation("com.github.fingerprintjs:fingerprint-android:2.2.0")

    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.14")

    implementation("kr.co.prnd:readmore-textview:1.0.0")
    implementation("money.vivid.elmslie:elmslie-android:3.0.0")
    // Room
    implementation("androidx.room:room-runtime:2.7.0")
    ksp("androidx.room:room-compiler:2.7.0")
    implementation("androidx.room:room-ktx:2.7.0")

    implementation("org.jetbrains.kotlinx:kotlinx-metadata-jvm:0.9.0")
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.5")

    // Зависимости на модули
    implementation(project(":ratingbrand"))
    implementation(project(":userprofile"))
}