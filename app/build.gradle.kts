plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.codingub.emergency"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.codingub.emergency"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.codingub.emergency.HiltTestRunner"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.4"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.1")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")


    // lifecycle
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0-rc01")

    // browser
    implementation("androidx.browser:browser:1.7.0")
    
    // compose
    implementation("androidx.navigation:navigation-compose:2.7.5")
    implementation("androidx.compose.foundation:foundation:1.5.4")

    // firebase
    implementation("com.google.firebase:firebase-storage:20.3.0")
    implementation("com.google.firebase:firebase-firestore:24.9.1")
    implementation("com.google.firebase:firebase-auth:22.3.0")

    // navigation bar
    implementation("com.exyte:animated-navigation-bar:1.0.0")

    // coil
    implementation("io.coil-kt:coil-compose:2.4.0")

    // hilt
    implementation("com.google.dagger:hilt-android:2.48.1")
    implementation("androidx.hilt:hilt-common:1.1.0")
    implementation("androidx.hilt:hilt-work:1.1.0")
    kapt("com.google.dagger:hilt-android-compiler:2.48.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
    kapt("androidx.hilt:hilt-compiler:1.1.0")

    // coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")

    // splash screen
    implementation("androidx.core:core-splashscreen:1.0.1")

    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // pager
    implementation("com.google.accompanist:accompanist-pager:0.32.0")
    implementation("com.google.accompanist:accompanist-pager-indicators:0.32.0")

    // lottie
    implementation("com.airbnb.android:lottie-compose:6.0.1")

    // room
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:$room_version") //для работы корутин
    annotationProcessor("androidx.room:room-compiler:$room_version")
    kapt("androidx.room:room-compiler:$room_version")

    // motion layout
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")

    // gson
    implementation("com.google.code.gson:gson:2.10.1")

    // exoplayer
    implementation("androidx.media3:media3-exoplayer:1.2.0")
    implementation("androidx.media3:media3-ui:1.2.0")

    implementation("androidx.compose.material:material-icons-extended:1.6.0-beta02")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")

    // permissions
    implementation("com.google.accompanist:accompanist-permissions:0.33.2-alpha")

    // location
    implementation("com.google.android.gms:play-services-location:21.0.1")

    // worker
    implementation("androidx.work:work-runtime-ktx:2.9.0")

    // shimmer
    implementation("com.facebook.shimmer:shimmer:0.5.0")

    // volley
    implementation("com.android.volley:volley:1.2.1")
    implementation("org.jsoup:jsoup:1.15.3")


    // test
    testImplementation("androidx.test:core-ktx:1.5.0")
    androidTestImplementation("androidx.test:runner:1.5.2")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    testImplementation("org.mockito:mockito-core:5.0.0")
    testImplementation("org.mockito:mockito-inline:5.0.0")
    testImplementation("org.mockito:mockito-android:5.0.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation("app.cash.turbine:turbine:0.12.1")
    testImplementation("androidx.work:work-testing:2.9.0")
    testImplementation("org.robolectric:robolectric:4.9")
    testImplementation("com.google.dagger:hilt-android-testing:2.48.1")
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.48.1")
    kaptTest("com.google.dagger:hilt-android-compiler:2.48.1")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.48.1")
}

kapt {
    correctErrorTypes = true
}