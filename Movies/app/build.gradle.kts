plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}


android {
    namespace = "ntu.duongthianhhong.blueymovies_64130758"
    compileSdk = 34

    defaultConfig {
        applicationId = "ntu.duongthianhhong.blueymovies"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
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

    buildFeatures {
        viewBinding = true
    }

    dependencies {
        implementation(libs.appcompat)
        implementation(libs.material)
        implementation(libs.activity)
        implementation(libs.constraintlayout)
        testImplementation(libs.junit)
        androidTestImplementation(libs.ext.junit)
        androidTestImplementation(libs.espresso.core)
        implementation("com.github.ismaeldivita:chip-navigation-bar:1.4.0")
        implementation("androidx.viewpager2:viewpager2:1.0.0")
        implementation("com.github.bumptech.glide:glide:4.12.0")
        implementation("com.github.Dimezis:BlurView:version-2.0.3")

        // Sử dụng BOM Firebase để quản lý các phụ thuộc Firebase
        implementation("com.google.firebase:firebase-database")
        implementation(platform("com.google.firebase:firebase-bom:32.8.0"))
        implementation("com.google.firebase:firebase-auth")
        implementation("com.google.firebase:firebase-firestore")
        implementation("com.google.firebase:firebase-storage:20.3.0")
    }
}
dependencies {
    implementation(libs.firebase.database)
}

