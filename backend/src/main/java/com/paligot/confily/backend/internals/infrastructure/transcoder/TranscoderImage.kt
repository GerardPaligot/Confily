package com.paligot.confily.backend.internals.infrastructure.transcoder

import kotlinx.coroutines.coroutineScope
import org.apache.batik.transcoder.SVGAbstractTranscoder
import org.apache.batik.transcoder.TranscoderInput
import org.apache.batik.transcoder.TranscoderOutput
import org.apache.batik.transcoder.image.PNGTranscoder
import org.xml.sax.SAXException
import java.io.ByteArrayOutputStream

class TranscoderImage {
    suspend fun convertSvgToPng(url: String, size: Int): Png = coroutineScope {
        val input = TranscoderInput(url)
        val stream = ByteArrayOutputStream()
        val transcoder = PNGTranscoder().apply {
            addTranscodingHint(SVGAbstractTranscoder.KEY_WIDTH, size.toFloat())
        }
        try {
            return@coroutineScope stream.use {
                val output = TranscoderOutput(it)
                transcoder.transcode(input, output)
                Png(size = size, content = it.toByteArray())
            }
        } catch (_: SAXException) {
            return@coroutineScope Png(size = size, content = null)
        }
    }

    companion object {
        const val SIZE_250 = 250
        const val SIZE_500 = 500
        const val SIZE_1000 = 1000
    }
}

class Png(
    val size: Int,
    val content: ByteArray?
)
