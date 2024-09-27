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
    
    func insertOrUpdateTicket(barcode: String) -> NativeSuspend<KotlinUnit, Error, KotlinUnit> {
        AgendaRepositoryNativeKt.insertOrUpdateTicket(self, barcode: barcode)
    }
    
    func event() -> NativeFlow<EventUi, Error, KotlinUnit> {
        AgendaRepositoryNativeKt.event(self)
    }
    
    func partners() -> NativeFlow<PartnerGroupsUi, Error, KotlinUnit> {
        AgendaRepositoryNativeKt.partners(self)
    }

    func partner(id: String) -> NativeFlow<PartnerItemUi, Error, KotlinUnit> {
        AgendaRepositoryNativeKt.partner(self, id: id)
    }
    
    func qanda() -> NativeFlow<Array<QuestionAndResponseUi>, Error, KotlinUnit> {
        AgendaRepositoryNativeKt.qanda(self)
    }
    
    func menus() -> NativeFlow<Array<MenuItemUi>, Error, KotlinUnit> {
        AgendaRepositoryNativeKt.menus(self)
    }
    
    func coc() -> NativeFlow<CoCUi, Error, KotlinUnit> {
        AgendaRepositoryNativeKt.coc(self)
    }
}
