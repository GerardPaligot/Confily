package org.gdglille.devfest.android.core.test.patterns.navigation

class RobotNavigator {
    var graph: NavRobotGraph = NavRobotGraph(mutableListOf())
        private set

    fun graph(graph: NavRobotGraph) {
        this.graph = graph
    }
}

inline fun <reified T : Any> RobotNavigator.navigateTo(): T = graph.destinationBy<T>()
