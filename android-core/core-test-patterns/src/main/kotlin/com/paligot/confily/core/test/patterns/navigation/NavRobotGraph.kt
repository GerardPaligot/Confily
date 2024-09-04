package com.paligot.confily.core.test.patterns.navigation

import kotlin.reflect.KClass

class NavRobotGraph(
    private val destination: MutableList<RobotDestination>
) {
    fun addDestination(destination: RobotDestination) {
        this.destination += destination
    }

    fun <T : Any> destinationBy(key: KClass<T>): T {
        return destination.first { it.keyDestination == key }.destination as T
    }
}

inline fun <reified T : Any> NavRobotGraph.destinationBy(): T = destinationBy(T::class)

inline fun <reified T : Any> NavRobotGraph.robot(destination: T) {
    addDestination(RobotDestination(T::class, destination))
}
