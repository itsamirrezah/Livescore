package com.itsamirrezah.livescore.ui.model

import com.itsamirrezah.livescore.data.models.Competition
import java.text.SimpleDateFormat
import java.util.*

open class ItemModel(
    var utcDate: String,
    var competition: Competition?
) {
    constructor(utcDate: String) : this(utcDate, null)

    open val dateTime: Date
        get() = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(utcDate)

    open val shortDate: Date
        get() = SimpleDateFormat("yyyy-MM-dd").parse(
            SimpleDateFormat("yyyy-MM-dd").format(dateTime)
        )
}