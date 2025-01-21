import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias (libs.plugins.dagger.hilt)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.org.jetbrains.kotlin.kapt)
    alias(libs.plugins.google.services)
    alias(libs.plugins.com.google.firebase.crashlytics)
    alias(libs.plugins.kotlin.parcelize)
    kotlin("plugin.serialization") version "1.8.10"
}

// Load keystore properties
val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties()

if (keystorePropertiesFile.exists()) {
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))
}

android {
    testOptions {
        unitTests.isIncludeAndroidResources = true
        unitTests.isReturnDefaultValues = true
    }

    signingConfigs {
        create("config") {
            storeFile = file(rootDir.canonicalPath + '/' + keystoreProperties["releaseKeyPassword"])
            storePassword = keystoreProperties["releaseKeyStore"] as String
            keyAlias = keystoreProperties["releaseKeyAlias"] as String
            keyPassword = keystoreProperties["releaseStorePassword"] as String
        }
        getByName("debug") {
            storeFile = file("../debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
    }
    namespace = "sa.sauditourism.employee"
    compileSdk = 34

    defaultConfig {
        applicationId = "sa.sauditourism.employee"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        multiDexEnabled = true
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            manifestPlaceholders["crashlyticsCollectionEnabled"] = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        debug {
            isMinifyEnabled = false
            isShrinkResources = false
            manifestPlaceholders["crashlyticsCollectionEnabled"] = true
            firebaseCrashlytics {
                // No need for crash reporting on debug build,
                // Disable it to speed up the build by disabling mapping file uploading.
                mappingFileUploadEnabled = false
            }
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
        freeCompilerArgs = (listOf("-Xsuppress-version-warnings"))
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
    flavorDimensions += listOf("default")
    productFlavors {
        register("alpha") {
            applicationIdSuffix = ".alpha"
            manifestPlaceholders["usesCleartextTraffic"] = true
            manifestPlaceholders  ["appLabel"] = "@string/app_name"
        }
        register("prod") {
            applicationIdSuffix = ""
            manifestPlaceholders["usesCleartextTraffic"] = true
            manifestPlaceholders  ["appLabel"] = "@string/app_name"
        }
    }
    packaging {
        resources.excludes.add("META-INF/*")
    }
}

dependencies {
    debugImplementation(libs.com.github.chuckerteam.chucker.library)
    releaseImplementation(libs.com.github.chuckerteam.chucker.library.no.op)

    implementation(libs.androidx.runtime.livedata)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.media3.exoplayer)
    implementation(libs.media3.ui)
    implementation(libs.media3.exoplayer.hls)
    implementation(libs.com.github.skydoves.landscapist.coil)
    implementation(libs.com.github.skydoves.landscapist.animation)
    implementation(libs.io.coil.kt.coil.svg)
    implementation(libs.play.services.ads.identifier)
    implementation(libs.converter.gson)
    implementation(libs.retrofit)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    //hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.work.runtime.ktx)
    kapt(libs.hilt.android.compiler)
    //firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.config.ktx)
    implementation(libs.firebase.crashlytics.ktx)
    implementation(libs.firebase.database.ktx)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    //huawei
    implementation(libs.huawei.auth)
    implementation(libs.huawei.push)
    implementation(libs.huawei.remote.config)
    implementation(libs.huawei.cloud.database)
    //retrofit
    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    //Glide
    implementation ("com.github.bumptech.glide:glide:4.16.0") {
        exclude( group = "com.android.support")
    }
    //root
    implementation (libs.rootbeer.lib)

    //auth
    implementation (libs.appauth)
    implementation (libs.core)

    //constrains
    implementation (libs.constrains.layout)

    //sdp
    implementation (libs.sdp)

    //Lottie
    implementation (libs.lottie)

    //System UI Controller
    implementation (libs.systemuicontroller)

    //phoneUtil
    implementation(libs.libphonenumber)

    //interceptor
    implementation (libs.logging.interceptor)

    //mockito
    implementation(libs.mockito.android)

    //Timber
    implementation(libs.timber)

    //Material
    implementation(libs.androidx.material)

    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.androidx.navigation.compose)
    testImplementation(libs.junit)
    testImplementation(libs.ui.test.junit4)
    testImplementation(libs.mockito.mockito.core)
    testImplementation(libs.mockk)
    testImplementation(libs.androidx.runner)
    testImplementation(libs.androidx.rules)
    // For Robolectric tests.
    testImplementation(libs.hilt.android.testing)
    testImplementation(libs.androidx.core.testing)
    testImplementation(libs.mockito.kotlin)
    // ...with Kotlin.
    kaptTest(libs.hilt.android.compiler)
    // ...with Java.
    testAnnotationProcessor(libs.hilt.android.compiler)
    androidTestImplementation(libs.mockito.android)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.mockito.mockito.core)
    androidTestImplementation(libs.mockito.kotlin)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    androidTestImplementation(libs.androidx.navigation.testing)
    androidTestImplementation(libs.androidx.espresso.intents)
    androidTestImplementation(libs.androidx.junit.v115)
    androidTestImplementation(libs.mockk)
    androidTestImplementation(libs.mockk.android)
    androidTestImplementation(libs.androidx.core.testing)
    androidTestImplementation(libs.mockk.agent)
    androidTestImplementation(libs.androidx.compose.ui.ui.test.junit4)
    // For instrumented tests.
    androidTestImplementation(libs.hilt.android.testing)
    // ...with Kotlin.
    kaptAndroidTest(libs.hilt.android.compiler)
    // ...with Java.
    androidTestAnnotationProcessor(libs.hilt.android.compiler)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    debugImplementation(libs.ui.test.manifest)

    //app update
    implementation (libs.appUpdate)

    implementation(libs.accompanist.permissions)

    androidTestImplementation(libs.androidx.compose.ui.ui.test.junit4)
    debugImplementation(libs.ui.test.manifest)

    testImplementation (libs.mockk)

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.gson)
    implementation ("androidx.compose.foundation:foundation:1.4.3")
    implementation ("com.google.accompanist:accompanist-pager-indicators:0.30.1")
}
kapt {
    correctErrorTypes = true
}