package com.paligot.confily.core.test.patterns.navigation

import kotlin.reflect.KClass

class RobotDestination(
    val keyDestination: KClass<*>,
    val destination: Any
)
