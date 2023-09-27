package org.gdglille.devfest.backend.internals.helpers.image

import kotlinx.coroutines.coroutineScope
import org.apache.batik.transcoder.SVGAbstractTranscoder
import org.apache.batik.transcoder.TranscoderInput
import org.apache.batik.transcoder.TranscoderOutput
import org.apache.batik.transcoder.image.PNGTranscoder
import org.xml.sax.SAXException
import java.io.ByteArrayOutputStream

class TranscoderImage {
    suspend fun convertSvgToPng(logoUrl: String, size: Int): Png = coroutineScope {
        val input = TranscoderInput(logoUrl)
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
        } catch (ignored: SAXException) {
            return@coroutineScope Png(size = size, content = null)
        }
    }
}

class Png(
    val size: Int,
    val content: ByteArray?
)
