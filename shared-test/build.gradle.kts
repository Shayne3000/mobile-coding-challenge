plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.senijoshua.shared_test"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    kotlinOptions {
        jvmTarget = "17"
    }

    packaging {
        resources.excludes.add("META-INF/*")
    }
}

dependencies {
    implementation(project(":app"))

    // Paging
    implementation(libs.paging.runtime)
    implementation(libs.paging.compose)

    // Kotlin Coroutine & Flows
    implementation(libs.kotlin.coroutines)

    // Compose Lifecycle
    implementation(libs.lifecycle.compose)

    // mockk
    implementation(libs.mockk)
    implementation(libs.mockk.agent)

    // Junit
    implementation(libs.junit)

    // Navigation testing
    implementation(libs.navigation.testing)
}
