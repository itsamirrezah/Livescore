package com.itsamirrezah.livescore.util.svg

import android.content.Context
import android.graphics.drawable.PictureDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import java.io.InputStream

@GlideModule
class SvgGlideModule : AppGlideModule() {
    override fun registerComponents(
        context: Context, glide: Glide, registry: Registry
    ) {
        registry.register(
            SvgOrImageDecodedResource::class.java,
            PictureDrawable::class.java,
            SvgOrImageDrawableTranscoder()
        )
            .append(
                InputStream::class.java,
                SvgOrImageDecodedResource::class.java,
                SvgOrImageDecoder()
            )
    }

    // Disable manifest parsing to avoid adding similar modules twice.
    override fun isManifestParsingEnabled(): Boolean {
        return false
    }
}