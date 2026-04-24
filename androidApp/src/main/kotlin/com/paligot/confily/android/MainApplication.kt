package com.paligot.confily.android

import android.app.Application
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.disk.DiskCache
import coil3.disk.directory
import coil3.memory.MemoryCache
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.svg.SvgDecoder
import com.paligot.confily.BuildKonfig
import com.paligot.confily.android.di.appModule
import io.openfeedback.viewmodels.OpenFeedbackFirebaseConfig
import io.openfeedback.viewmodels.initializeOpenFeedback
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin

private const val MemoryCacheSize = 0.25
private const val DiskCacheSize = 0.10

class MainApplication : Application(), SingletonImageLoader.Factory, KoinComponent {
    override fun onCreate() {
        super.onCreate()
        initializeOpenFeedback(
            OpenFeedbackFirebaseConfig(
                context = this,
                projectId = BuildKonfig.FIREBASE_PROJECT_ID,
                applicationId = BuildKonfig.APP_ID,
                apiKey = BuildKonfig.FIREBASE_API_KEY,
                databaseUrl = "https://${BuildKonfig.FIREBASE_PROJECT_ID}.firebaseio.com"
            )
        )

        startKoin {
            androidContext(this@MainApplication)
            workManagerFactory()
            modules(appModule)
        }

        val workManager = WorkManager.getInstance(this)
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val request = OneTimeWorkRequestBuilder<ScheduleWorkManager>()
            .setConstraints(constraints)
            .build()
        workManager.enqueue(request)
    }

    override fun newImageLoader(context: PlatformContext): ImageLoader =
        ImageLoader.Builder(context)
            .components {
                add(SvgDecoder.Factory())
                add(KtorNetworkFetcherFactory())
            }
            .memoryCache {
                MemoryCache.Builder()
                    .maxSizePercent(context, MemoryCacheSize)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(context.cacheDir.resolve("image_cache"))
                    .maxSizePercent(DiskCacheSize)
                    .build()
            }
            .build()
}
