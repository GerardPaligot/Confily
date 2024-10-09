package com.paligot.confily.wear.presentation

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.disk.DiskCache
import coil3.disk.directory
import coil3.memory.MemoryCache
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.svg.SvgDecoder
import com.paligot.confily.core.di.platformModule
import com.paligot.confily.core.di.repositoriesModule
import com.paligot.confily.wear.buildConfigModule
import com.paligot.confily.wear.events.di.eventsModule
import com.paligot.confily.wear.presentation.main.mainModule
import com.paligot.confily.wear.presentation.partners.partnersModule
import com.paligot.confily.wear.schedules.di.schedulesModule
import com.paligot.confily.wear.speakers.di.speakersModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

private const val MemoryCacheSize = 0.25
private const val DiskCacheSize = 0.10

class MainApplication : Application(), SingletonImageLoader.Factory {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MainApplication)
            modules(
                buildConfigModule,
                platformModule,
                repositoriesModule,
                mainModule,
                eventsModule,
                schedulesModule,
                speakersModule,
                partnersModule
            )
        }
    }

    override fun newImageLoader(context: PlatformContext): ImageLoader {
        return ImageLoader.Builder(context)
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
}
