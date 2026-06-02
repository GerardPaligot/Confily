-keep class com.paligot.confily.models.** { *; }
-keep class io.openfeedback.android.model.** { *; }

# Room finds *_Impl classes via reflection and calls their no-arg constructor.
-keep class * extends androidx.room.RoomDatabase { <init>(); }

-keepattributes Signature

# Needed by OkHttp 4 and under.
# I don't use OkHttp in my dependency.
# Probably a transitive dependency.
-dontwarn org.bouncycastle.jsse.BCSSLSocket
-dontwarn org.bouncycastle.jsse.BCSSLParameters
-dontwarn org.bouncycastle.jsse.provider.BouncyCastleJsseProvider
-dontwarn org.conscrypt.*
-dontwarn org.openjsse.javax.net.ssl.SSLParameters
-dontwarn org.openjsse.javax.net.ssl.SSLSocket
-dontwarn org.openjsse.net.ssl.OpenJSSE
-dontwarn org.slf4j.impl.StaticLoggerBinder

# See https://github.com/firebase/firebase-android-sdk/issues/2124
-keep class com.google.android.gms.internal.** { *; }

# ML Kit resolves its barcode scanner via an internal component registry
# (CommonComponentRegistrar + MlKitContext). R8 full mode over-shrinks this
# wiring, so BarcodeScanning.getClient() returns a component with a null field
# and the scan-ticket screen crashes in release only. The barcode-scanning AAR's
# consumer rules cover the gms.internal side (already kept above) but not the
# com.google.mlkit.* package itself.
-keep class com.google.mlkit.** { *; }