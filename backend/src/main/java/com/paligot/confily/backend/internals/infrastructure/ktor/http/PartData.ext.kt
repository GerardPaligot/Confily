package com.paligot.confily.backend.internals.infrastructure.ktor.http

import io.ktor.http.content.PartData
import io.ktor.server.plugins.BadRequestException
import io.ktor.utils.io.jvm.javaio.toInputStream
import java.io.File

fun PartData?.asFile(): File {
    if (this !is PartData.FileItem) {
        throw BadRequestException("PartData is not a file")
    }
    try {
        val fileName = this.originalFileName ?: "uploaded_file"
        val file = File(fileName)
        this.provider().toInputStream().use { input ->
            file.outputStream().buffered().use { output ->
                input.copyTo(output)
            }
        }
        return file
    } finally {
        this.dispose()
    }
}
