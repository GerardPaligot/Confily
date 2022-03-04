package com.paligot.conferences.android.screens.scanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.paligot.conferences.models.UserNetworkingUi
import com.paligot.conferences.repositories.UserRepository
import kotlinx.coroutines.launch

class QrCodeScannerViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun saveNetworkingProfile(user: UserNetworkingUi) = viewModelScope.launch {
        userRepository.insertNetworkingProfile(user)
    }

    object Factory {
        fun create(userRepository: UserRepository) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                QrCodeScannerViewModel(userRepository = userRepository) as T
        }
    }
}