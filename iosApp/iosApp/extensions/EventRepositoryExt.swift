//
//  EventRepositoryExt.swift
//  iosApp
//
//  Created by Gérard PALIGOT on 11/08/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SharedDi
import KMPNativeCoroutinesCore

extension EventRepository {
    func fetchAndStoreEventList() -> NativeSuspend<KotlinUnit, Error, KotlinUnit> {
        EventRepositoryNativeKt.fetchAndStoreEventList(self)
    }
    
    func events() -> NativeFlow<EventItemListUi, Error, KotlinUnit> {
        EventRepositoryNativeKt.events(self)
    }
}
