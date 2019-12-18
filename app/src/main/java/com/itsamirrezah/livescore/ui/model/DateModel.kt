package com.itsamirrezah.livescore.ui.model

import java.text.SimpleDateFormat

class DateModel(
    utcDate: String
) : ItemModel(utcDate){

    val dayOfWeek: String
        get() = SimpleDateFormat("E").format(dateTime.time)

    val dateOfMonth: String
        get() = SimpleDateFormat("MMMM dd").format(dateTime.time)
}