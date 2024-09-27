//
//  SchedulesRepositoryExt.swift
//  iosApp
//
//  Created by Gérard PALIGOT on 27/09/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SharedDi
import KMPNativeCoroutinesCore

extension SchedulesRepository {
    func agenda() -> NativeFlow<Dictionary<String, AgendaUi>, Error, KotlinUnit> {
        SchedulesRepositoryNativeKt.agenda(self)
    }
    
    func filters() -> NativeFlow<FiltersUi, Error, KotlinUnit> {
        SchedulesRepositoryNativeKt.filters(self)
    }
    
    func scheduleItem(scheduleId: String) -> NativeFlow<TalkUi, Error, KotlinUnit> {
        SchedulesRepositoryNativeKt.scheduleItem(self, scheduleId: scheduleId)
    }
    
    func scheduleEventSessionItem(scheduleId: String) -> NativeFlow<EventSessionItemUi, Error, KotlinUnit> {
        SchedulesRepositoryNativeKt.scheduleEventSessionItem(self, scheduleId: scheduleId)
    }
}
