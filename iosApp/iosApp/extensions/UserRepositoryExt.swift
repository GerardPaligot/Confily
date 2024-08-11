//
//  UserRepositoryExt.swift
//  iosApp
//
//  Created by Gérard PALIGOT on 11/08/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SharedDi
import KMPNativeCoroutinesCore

extension UserRepository {
    func fetchProfile() -> NativeFlow<UserProfileUi?, Error, KotlinUnit> {
        UserRepositoryNativeKt.fetchProfile(self)
    }
    
    func fetchNetworking() -> NativeFlow<Array<UserNetworkingUi>, Error, KotlinUnit> {
        UserRepositoryNativeKt.fetchNetworking(self)
    }
}
