-keep class org.gdglille.devfest.models.** { *; }
-keep class io.openfeedback.android.model.** { *; }

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