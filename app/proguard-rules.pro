# Please add these rules to your existing keep rules in order to suppress warnings.
# This is generated automatically by the Android Gradle plugin.
-dontwarn org.bouncycastle.jsse.BCSSLParameters
-dontwarn org.bouncycastle.jsse.BCSSLSocket
-dontwarn org.bouncycastle.jsse.provider.BouncyCastleJsseProvider
-dontwarn org.conscrypt.Conscrypt$Version
-dontwarn org.conscrypt.Conscrypt
-dontwarn org.conscrypt.ConscryptHostnameVerifier
-dontwarn org.openjsse.javax.net.ssl.SSLParameters
-dontwarn org.openjsse.javax.net.ssl.SSLSocket
-dontwarn org.openjsse.net.ssl.OpenJSSE

# Preserve AndroidX Core (ktx)
-keep class androidx.core.** { *; }
-dontwarn androidx.core.**

# Preserve AndroidX AppCompat
-keep class androidx.appcompat.** { *; }
-dontwarn androidx.appcompat.**

# Preserve Material Components (Material Design Library)
-keep class com.google.android.material.** { *; }
-dontwarn com.google.android.material.**

# Preserve JUnit for testing
-keep class org.junit.** { *; }
-dontwarn org.junit.**

# Preserve AndroidX JUnit
-keep class androidx.test.ext.junit.** { *; }
-dontwarn androidx.test.ext.junit.**

# Preserve Espresso Core for UI testing
-keep class androidx.test.espresso.** { *; }
-dontwarn androidx.test.espresso.**

# Optional: General rule to preserve public methods in all libraries
# This prevents removing or obfuscating any public method or class from these libraries
-keepclassmembers class ** {
    public *;
}
