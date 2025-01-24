plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.apollo)
}

android {
    namespace = "me.pood1e.collector4"
    compileSdk = 35

    defaultConfig {
        applicationId = "me.pood1e.collector4"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.apollo.runtime)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.play.services.wearable)
}

apollo {
    service("service") {
        packageName.set("me.pood1e.collector4")
        val token: String by project
        introspection {
            endpointUrl.set("https://api.github.com/graphql")
            headers.put("Authorization", "Bearer $token")
            schemaFile.set(file("src/main/graphql/schema.graphqls"))
        }
    }
}