package com.paligot.confily.core.di

import org.koin.core.module.Module

const val IsDebugNamed = "IsDebug"
const val ApplicationIdNamed = "ApplicationId"
const val AcceptLanguageNamed = "Accept-Language"
const val ConfilyBaseUrlNamed = "ConfilyBaseUrl"
const val TempFolderPathNamed = "TempFolderPath"
const val VersionCodeNamed = "VersionCode"

expect val platformModule: Module
