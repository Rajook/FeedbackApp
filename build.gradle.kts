// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.hilt.android) apply false // Add this line for Hilt
}

buildscript {
    dependencies {
        classpath (libs.hilt.android    .gradle.plugin)
    }
}