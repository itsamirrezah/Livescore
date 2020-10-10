package com.itsamirrezah.livescore.ui.model

import com.itsamirrezah.livescore.util.Utils

class MatchModel(
    val homeTeam: TeamModel,
    val homeTeamScore: String,
    val awayTeam: TeamModel,
    val awayTeamScore: String,
    utcDate: String,
    val status: String,
    competition: CompetitionUi,
    val matchday: String
) : ItemModel(utcDate, competition) {

    val shortTime: String
        get() = Utils.clockFormat(dateTime)
}