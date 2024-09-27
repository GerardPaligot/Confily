//
//  AgendaRepositoryExt.swift
//  iosApp
//
//  Created by Gérard PALIGOT on 11/08/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SharedDi
import KMPNativeCoroutinesCore

extension AgendaRepository {
    func fetchAndStoreAgenda() -> NativeSuspend<KotlinUnit, Error, KotlinUnit> {
        AgendaRepositoryNativeKt.fetchAndStoreAgenda(self)
    }
}
