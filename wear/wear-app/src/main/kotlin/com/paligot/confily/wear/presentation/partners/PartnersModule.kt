package com.paligot.confily.wear.presentation.partners

import com.paligot.confily.wear.presentation.partners.presentation.PartnerViewModel
import com.paligot.confily.wear.presentation.partners.presentation.PartnersViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val partnersModule = module {
    viewModel { PartnersViewModel(repository = get()) }
    viewModel { params -> PartnerViewModel(partnerId = params.get(), repository = get()) }
}
