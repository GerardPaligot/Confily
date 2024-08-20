package org.gdglille.devfest.android.core.test.patterns.navigation

import kotlin.reflect.KClass

class RobotDestination(
    val keyDestination: KClass<*>,
    val destination: Any
)
