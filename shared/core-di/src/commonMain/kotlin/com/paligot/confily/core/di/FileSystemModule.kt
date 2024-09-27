package com.paligot.confily.core.di

import com.paligot.confily.core.fs.ConferenceFileSystem
import org.koin.core.qualifier.named
import org.koin.dsl.module

val fileSystemModule = module {
    includes(platformModule)
    single { ConferenceFileSystem.create(tempFolderPath = get(named(TempFolderPath))) }
}
