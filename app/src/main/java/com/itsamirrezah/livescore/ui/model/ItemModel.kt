package com.itsamirrezah.livescore.ui.model

import com.itsamirrezah.livescore.util.Utils
import org.threeten.bp.LocalDate
import java.util.*

open class ItemModel(
    var utcDate: String,
    var competition: CompetitionUi?
) {
    constructor(utcDate: String) : this(utcDate, null)

    open val dateTime: Date
        get() = Utils.toDate(utcDate)!!

    open val shortDate: Date
        get() = Utils.toDate(localDate)

    open val localDate: LocalDate
        get() = Utils.toLocalDate(utcDate)
}