package com.paligot.confily.core.test

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry

class FakeLifecycleOwner : LifecycleOwner {
    private val lifecycleRegistry = LifecycleRegistry(this).apply {
        currentState = Lifecycle.State.RESUMED // Simulate an active lifecycle
    }

    override val lifecycle: Lifecycle
        get() = lifecycleRegistry
}
