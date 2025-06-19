// Top-level build file - ONLY for plugin management and project-wide configurations
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
}

// Optional: Define project-wide variables
buildscript {
    repositories {
        google()
        mavenCentral()
    }
}


// Remove ALL dependencies from here - they belong in module-level files