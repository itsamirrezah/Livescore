package com.itsamirrezah.livescore.ui.model

import com.google.gson.annotations.SerializedName

class CompetitionModel(
    utcDate: String,
    competition: CompetitionUi?,
    var matchday: String
) : ItemModel(utcDate, competition) {

    val competitionName: String
        get() = competition!!.name
}

data class CompetitionUi(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)