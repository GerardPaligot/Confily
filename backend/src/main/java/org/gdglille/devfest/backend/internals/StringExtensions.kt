package org.gdglille.devfest.backend.internals

import java.util.Locale

fun String.slug() = lowercase(Locale.ROOT)
    .replace("\n", " ")
    .replace("[^a-z\\d\\s]".toRegex(), " ")
    .split(" ")
    .joinToString("-")
    .replace("-+".toRegex(), "-")
