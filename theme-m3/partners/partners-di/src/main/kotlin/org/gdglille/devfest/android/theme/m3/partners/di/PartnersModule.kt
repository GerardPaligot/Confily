package org.gdglille.devfest.android.theme.m3.partners.di

import com.paligot.confily.core.di.repositoriesModule
import org.gdglille.devfest.android.theme.m3.partners.feature.PartnerDetailViewModel
import org.gdglille.devfest.android.theme.m3.partners.feature.PartnersViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val partnersModule = module {
    includes(repositoriesModule)
    viewModel { PartnersViewModel(get()) }
    viewModel { parametersHolder -> PartnerDetailViewModel(parametersHolder.get(), get()) }
}
