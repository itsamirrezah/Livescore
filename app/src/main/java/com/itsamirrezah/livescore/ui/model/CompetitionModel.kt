package com.itsamirrezah.livescore.ui.model

import com.itsamirrezah.livescore.data.models.Competition

class CompetitionModel(
    utcDate: String,
    competition: Competition?,
    var matchday: String
) : ItemModel(utcDate, competition) {

    val competitionName: String
        get() = competition!!.name
}