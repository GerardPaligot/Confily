package org.gdglille.devfest.models.ui

data class CategoryUi(
    val id: String,
    val name: String,
    val color: String,
    val icon: String?
) {
    companion object {
        val fake = CategoryUi(id = "", name = "Cloud", color = "default", icon = null)
    }
}
