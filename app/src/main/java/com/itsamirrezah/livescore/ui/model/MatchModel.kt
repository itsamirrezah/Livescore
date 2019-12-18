package com.itsamirrezah.livescore.ui.model

import com.itsamirrezah.livescore.data.models.Competition
import java.text.SimpleDateFormat

class MatchModel(
    val homeTeam: String,
    val homeTeamScore: String,
    val awayTeam: String,
    val awayTeamScore: String,
    utcDate: String,
    val status: String,
    competition: Competition,
    val matchday: String
) : ItemModel(utcDate,competition){

    val shortTime: String
        get() = SimpleDateFormat("HH:mm").format(dateTime)
}