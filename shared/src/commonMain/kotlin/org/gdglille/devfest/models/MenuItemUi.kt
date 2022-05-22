package org.gdglille.devfest.models

data class MenuItemUi(
    val name: String,
    val dish: String,
    val accompaniment: String,
    val dessert: String
) {
    companion object {
        val fake = MenuItemUi(
            name = "Lunch bag Gourmande",
            dish = "Wrap dinde, celeri, salade frisée, Cheddar",
            accompaniment = "Salade de concombre, chèvre frais, menthe",
            dessert = "Riz au lait à la framboise\n"
        )
    }
}
