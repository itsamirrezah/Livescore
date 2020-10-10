package com.itsamirrezah.livescore.ui.model

import android.graphics.drawable.Drawable

data class TeamModel(
    val id: Int,
    val name: String
) {
    var flag: String? = null
    var flagDrawable: Drawable? = null

    constructor(id: Int, name: String, flag: String?) : this(id, name) {
        this.flag = flag
    }
}