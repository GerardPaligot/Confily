package org.gdglille.devfest.android

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.SvgDecoder
import coil.disk.DiskCache
import coil.memory.MemoryCache
import io.openfeedback.android.OpenFeedbackConfig

private const val MemoryCacheSize = 0.25
private const val DiskCacheSize = 0.10

class MainApplication : Application(), ImageLoaderFactory {
    lateinit var openFeedbackConfig: OpenFeedbackConfig

    override fun onCreate() {
        super.onCreate()
        openFeedbackConfig = OpenFeedbackConfig(
            context = this,
            firebaseConfig = OpenFeedbackConfig.FirebaseConfig(
                projectId = BuildConfig.FIREBASE_PROJECT_ID,
                applicationId = BuildConfig.APPLICATION_ID,
                apiKey = BuildConfig.FIREBASE_API_KEY,
                databaseUrl = "https://${BuildConfig.FIREBASE_PROJECT_ID}.firebaseio.com"
            )
        )
    }

    override fun newImageLoader(): ImageLoader = ImageLoader.Builder(this)
        .components {
            add(SvgDecoder.Factory())
        }
        .memoryCache {
            MemoryCache.Builder(this)
                .maxSizePercent(MemoryCacheSize)
                .build()
        }
        .diskCache {
            DiskCache.Builder()
                .directory(this.cacheDir.resolve("image_cache"))
                .maxSizePercent(DiskCacheSize)
                .build()
        }
        .respectCacheHeaders(false)
        .build()
}
