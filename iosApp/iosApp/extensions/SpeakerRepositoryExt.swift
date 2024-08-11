//
//  SpeakerRepositoryExt.swift
//  iosApp
//
//  Created by Gérard PALIGOT on 11/08/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SharedDi
import KMPNativeCoroutinesCore

extension SpeakerRepository {
    func speakers() -> NativeFlow<Array<SpeakerItemUi>, Error, KotlinUnit> {
        SpeakerRepositoryNativeKt.speakers(self)
    }
}
