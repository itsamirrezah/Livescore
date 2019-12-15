package com.itsamirrezah.livescore.ui.model

import com.itsamirrezah.livescore.data.models.Competition
import java.util.*

class MatchModel(
    val homeTeam: String,
    val homeTeamScore: String,
    val awayTeam: String,
    val awayTeamScore: String,
    val utcDate: Date,
    val status: String,
    val competition: Competition,
    val matchday: Int
) : ItemModel()