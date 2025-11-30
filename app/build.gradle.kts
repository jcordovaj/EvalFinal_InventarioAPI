plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
}

android {
    namespace  = "com.mod6.evalfinal_inventarioapi"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mod6.evalfinal_inventarioapi"
        minSdk      = 26
        targetSdk   = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.mod6.evalfinal_inventarioapi.CustomTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

dependencies {

    // --- Core y UI ---
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.recyclerview)

    // --- Navigation ---
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // --- Lifecycle y ViewModel ---
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // --- Room (Base de Datos) ---
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)

    // NOTA: Estas no son dependencias de Room, pero se mantienen en `implementation`:
    implementation(libs.androidx.junit.ktx)
    implementation(libs.androidx.benchmark.common)

    ksp(libs.androidx.room.compiler)

    // --- WorkManager ---
    implementation(libs.androidx.work.runtime.ktx)

    // --- Hilt (Inyección de Dependencias) ---
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Hilt Extensions
    implementation(libs.androidx.hilt.work)
    ksp(libs.androidx.hilt.compiler)

    // --- Coroutines ---
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)


    // --- LIBRERÍAS DE NETWORKING (Retrofit, OkHttp, Gson) ---
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp.core)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.google.gson)

    // =========================================================
    // --- TESTING UNITARIO (src/test - JVM) ---
    // =========================================================
    testImplementation(libs.junit4)

    // Auxiliares y Mocking
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.androidx.arch.core.testing)
    testImplementation(libs.androidx.room.testing)
    testImplementation(libs.io.mockk)

    // Coroutines
    testImplementation(libs.kotlinx.coroutines.test)

    // Testing de Room
    testImplementation(libs.androidx.room.runtime) // Mantener si la prueba de Room lo requiere

    // KSP para el Hilt y Room testing
    kspTest(libs.hilt.compiler)
    kspTest(libs.androidx.hilt.compiler)
    kspTest(libs.androidx.room.compiler)

    // =========================================================
    // --- TESTING DE INSTRUMENTACIÓN (androidTest - Android Device) ---
    // =========================================================

    // JUnit y Espresso
    androidTestImplementation(libs.androidx.test.ext.junit)      // androidx-test-ext-junit (asumimos que lo renombraste)
    androidTestImplementation(libs.androidx.test.espresso.core) // androidx-test-espresso-core (asumimos que lo renombraste)
    androidTestImplementation(libs.androidx.junit.ktx)

    // Auxiliares de Testing (AndroidX)
    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.androidx.arch.core.testing)
    androidTestImplementation(libs.androidx.room.testing)

    // Hilt testing
    androidTestImplementation(libs.hilt.android.testing)
    kspAndroidTest(libs.hilt.compiler)

    // Utilidades (Si se necesitan en el ambiente androidTest)
    androidTestImplementation(libs.androidx.core.ktx)

    // Coroutines
    androidTestImplementation(libs.kotlinx.coroutines.test) // NECESARIO para MainDispatcherRule

}