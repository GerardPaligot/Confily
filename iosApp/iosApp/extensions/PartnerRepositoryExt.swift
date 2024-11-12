//
//  PartnerRepositoryExt.swift
//  iosApp
//
//  Created by Gérard PALIGOT on 27/09/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SharedDi
import KMPNativeCoroutinesCore

extension PartnerRepository {
    func partners() -> NativeFlow<PartnersActivitiesUi, Error, KotlinUnit> {
        PartnerRepositoryNativeKt.partners(self)
    }

    func partner(id: String) -> NativeFlow<PartnerItemUi, Error, KotlinUnit> {
        PartnerRepositoryNativeKt.partner(self, id: id)
    }
}
