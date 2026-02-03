# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# signingConfig, minifyEnabled and shrinkResources flags in build.gradle.

# Retrofit
-keep class com.squareup.okhttp3.** { *; }
-keep interface com.squareup.okhttp3.** { *; }
-dontwarn com.squareup.okhttp3.**

-keep class retrofit2.** { *; }
-dontwarn retrofit2.**

-keep class com.google.gson.** { *; }
-dontwarn com.google.gson.**

# GVR
-keep class com.google.vr.** { *; }
-dontwarn com.google.vr.**

# ExoPlayer
-keep class androidx.media3.** { *; }
-dontwarn androidx.media3.**
