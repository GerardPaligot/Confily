package com.paligot.confily.core.models.factory

import com.paligot.confily.models.Category

fun Category.Companion.builder(): CategoryBuilder = CategoryBuilder()

class CategoryBuilder {
    private var id: String = ""
    private var name: String = ""
    private var color: String = ""
    private var icon: String = ""

    fun id(id: String) = apply { this.id = id }
    fun name(name: String) = apply { this.name = name }
    fun color(color: String) = apply { this.color = color }
    fun icon(icon: String) = apply { this.icon = icon }

    fun build(): Category = Category(
        id = id,
        name = name,
        color = color,
        icon = icon
    )
}
