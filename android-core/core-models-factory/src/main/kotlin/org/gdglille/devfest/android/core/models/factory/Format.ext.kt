package org.gdglille.devfest.android.core.models.factory

import org.gdglille.devfest.models.Format

fun Format.Companion.builder(): FormatBuilder = FormatBuilder()

class FormatBuilder {
    private var id: String = ""
    private var name: String = ""
    private var time: Int = 0

    fun id(id: String) = apply { this.id = id }
    fun name(name: String) = apply { this.name = name }
    fun time(time: Int) = apply { this.time = time }

    fun build(): Format = Format(
        id = id,
        name = name,
        time = time
    )
}
