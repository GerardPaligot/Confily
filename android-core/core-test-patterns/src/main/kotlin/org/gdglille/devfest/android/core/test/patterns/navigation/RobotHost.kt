package org.gdglille.devfest.android.core.test.patterns.navigation

fun robotHost(navigator: RobotNavigator, block: NavRobotGraph.() -> Unit) {
    val graph = NavRobotGraph(mutableListOf()).apply(block)
    navigator.graph(graph)
}
