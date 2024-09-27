//
//  UserRepositoryExt.swift
//  iosApp
//
//  Created by Gérard PALIGOT on 11/08/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SharedDi
import KMPNativeCoroutinesCore

extension NetworkingRepository {
    func fetchProfile() -> NativeFlow<UserProfileUi?, Error, KotlinUnit> {
        NetworkingRepositoryNativeKt.fetchProfile(self)
    }
    
    func fetchNetworking() -> NativeFlow<Array<UserNetworkingUi>, Error, KotlinUnit> {
        NetworkingRepositoryNativeKt.fetchNetworking(self)
    }
}
