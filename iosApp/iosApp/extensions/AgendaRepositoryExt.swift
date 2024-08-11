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
    
    func agenda() -> NativeFlow<Dictionary<String, AgendaUi>, Error, KotlinUnit> {
        AgendaRepositoryNativeKt.agenda(self)
    }
    
    func filters() -> NativeFlow<FiltersUi, Error, KotlinUnit> {
        AgendaRepositoryNativeKt.filters(self)
    }
    
    func scheduleItem(scheduleId: String) -> NativeFlow<TalkUi, Error, KotlinUnit> {
        AgendaRepositoryNativeKt.scheduleItem(self, scheduleId: scheduleId)
    }
    
    func scheduleEventSessionItem(scheduleId: String) -> NativeFlow<EventSessionItemUi, Error, KotlinUnit> {
        AgendaRepositoryNativeKt.scheduleEventSessionItem(self, scheduleId: scheduleId)
    }
    
    func speaker(speakerId: String) -> NativeFlow<SpeakerUi, Error, KotlinUnit> {
        AgendaRepositoryNativeKt.speaker(self, speakerId: speakerId)
    }
}
