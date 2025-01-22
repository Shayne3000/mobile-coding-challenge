plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.room)
}

android {
    namespace = "com.senijoshua.pods"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.senijoshua.pods"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.senijoshua.pods.util.HiltTestRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
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

    room {
        schemaDirectory("$projectDir/schemas")
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        buildConfig = true
    }

    packaging {
        resources.excludes.add("META-INF/*")
    }
}

dependencies {
    // Kotlin extensions & core Android
    implementation(libs.core.ktx)
    implementation(libs.activity.compose)
    implementation(libs.lifecycle.compose)

     // Hilt
     implementation(libs.hilt)
     ksp(libs.hilt.compiler)
     implementation(libs.hilt.navigation.compose)

    // Compose UI
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.material3)
    implementation(libs.navigation)

    // Room //
    implementation(libs.room)
    ksp(libs.room.compiler)
    // Kotlin Extensions and Coroutines support for Room
    implementation(libs.room.ktx)
    // Paging support for Room
    implementation(libs.room.paging)

    // Coil
    implementation(libs.coil)

    // Paging
    implementation(libs.paging.runtime)
    implementation(libs.paging.compose)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.kotlin.converter)

    // Kotlin Serialization
    implementation(libs.kotlin.serialization.json)

    // OkHttp
    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)

    // Kotlin Coroutine & Flows
    implementation(libs.kotlin.coroutines)

    //// Test dependencies ////

    // Shared test components
    testImplementation(project(":shared-test"))
    androidTestImplementation(project(":shared-test"))

    // Local tests
    testImplementation(libs.coil.testing)
    testImplementation(libs.hilt.android.testing)
    kspTest(libs.hilt.compiler)
    testImplementation(libs.room.testing)
    testImplementation(libs.paging.test)
    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(libs.junit)
    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

    // Instrumented tests
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.hilt.android.testing)
    androidTestImplementation(libs.test.runner)
    kspAndroidTest(libs.hilt.compiler)
    androidTestImplementation(libs.navigation.testing)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
}
