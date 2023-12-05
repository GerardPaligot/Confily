package org.gdglille.devfest.android.theme.m3.partners.di

import org.gdglille.devfest.android.theme.m3.partners.feature.PartnerDetailViewModel
import org.gdglille.devfest.android.theme.m3.partners.feature.PartnersViewModel
import org.gdglille.devfest.repositoriesModule
import org.koin.dsl.module

val partnersModule = module {
    includes(repositoriesModule)
    single { PartnersViewModel(get()) }
    single { parametersHolder -> PartnerDetailViewModel(parametersHolder.get(), get()) }
}
