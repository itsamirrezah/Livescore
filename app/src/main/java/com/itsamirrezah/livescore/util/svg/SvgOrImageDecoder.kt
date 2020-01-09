package com.itsamirrezah.livescore.util.svg

import android.graphics.BitmapFactory
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.ResourceDecoder
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.resource.SimpleResource
import com.caverock.androidsvg.SVG
import com.caverock.androidsvg.SVGParseException
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream

class SvgOrImageDecoder : ResourceDecoder<InputStream, SvgOrImageDecodedResource> {

    override fun handles(source: InputStream, options: Options): Boolean {
        return true
    }

    @Throws(IOException::class)
    override fun decode(
        source: InputStream, width: Int, height: Int,
        options: Options
    ): Resource<SvgOrImageDecodedResource>? {
        val array = source.readBytes()
        val svgInputStream = ByteArrayInputStream(array.clone())
        val pngInputStream = ByteArrayInputStream(array.clone())

        return try {
            val svg = SVG.getFromInputStream(svgInputStream)

            try {
                source.close()
                pngInputStream.close()
            } catch (e: IOException) {
            }

            SimpleResource(SvgOrImageDecodedResource(svg))
        } catch (ex: SVGParseException) {
            try {
                val bitmap = BitmapFactory.decodeStream(pngInputStream)
                SimpleResource(SvgOrImageDecodedResource(bitmap = bitmap))
            } catch (exception: Exception) {
                try {
                    source.close()
                    pngInputStream.close()
                } catch (e: IOException) {
                }
                throw IOException("Cannot load SVG or Image from stream", ex)
            }
        }
    }
}
