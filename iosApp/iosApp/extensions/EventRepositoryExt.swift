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
    
    func event() -> NativeFlow<EventUi, Error, KotlinUnit> {
        EventRepositoryNativeKt.eventAndTicket(self)
    }
    
    func qanda() -> NativeFlow<Array<QuestionAndResponseUi>, Error, KotlinUnit> {
        EventRepositoryNativeKt.qanda(self)
    }
    
    func menus() -> NativeFlow<Array<MenuItemUi>, Error, KotlinUnit> {
        EventRepositoryNativeKt.menus(self)
    }
    
    func coc() -> NativeFlow<CoCUi, Error, KotlinUnit> {
        EventRepositoryNativeKt.coc(self)
    }
    
    func insertOrUpdateTicket(barcode: String) -> NativeSuspend<KotlinUnit, Error, KotlinUnit> {
        EventRepositoryNativeKt.insertOrUpdateTicket(self, barcode: barcode)
    }
}
