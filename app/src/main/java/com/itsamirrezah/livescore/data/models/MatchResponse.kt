package com.itsamirrezah.livescore.data.models

import com.google.gson.annotations.SerializedName

data class MatchResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("filters")
    val filters: Filters,
    @SerializedName("matches")
    val matches: List<Match>
)


data class Filters(
    @SerializedName("dateFrom")
    val dateFrom: String,
    @SerializedName("dateTo")
    val dateTo: String,
    @SerializedName("permission")
    val permission: String
)

data class Match(
    @SerializedName("id")
    val id: Int,
    @SerializedName("competition")
    val competition: Competition,
    @SerializedName("season")
    val season: Season,
    @SerializedName("utcDate")
    val utcDate: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("matchday")
    val matchday: Int,
    @SerializedName("stage")
    val stage: String,
    @SerializedName("group")
    val group: String,
    @SerializedName("lastUpdated")
    val lastUpdated: String,
    @SerializedName("score")
    val score: Score,
    @SerializedName("homeTeam")
    val homeTeam: Team,
    @SerializedName("awayTeam")
    val awayTeam: Team

)

data class Season(
    @SerializedName("id")
    val id: Int,
    @SerializedName("startDate")
    val startDate: String,
    @SerializedName("endDate")
    val endDate: String,
    @SerializedName("currentMatchday")
    val currentMatchday: Int,
    @SerializedName("winner")
    val winner: String
)

data class Team(
    @SerializedName("id")
     val id: Int,
    @SerializedName("name")
     val name: String
)

data class Score(
    @SerializedName("winner")
    val winner: String,
    @SerializedName("duration")
    val duration: String,
    @SerializedName("fullTime")
    val fullTime: TeamScore,
    @SerializedName("penalties")
    val penalties: TeamScore
)

data class TeamScore(
    @SerializedName("homeTeam")
    val homeTeam: Int,
    @SerializedName("awayTeam")
    val awayTeam: Int
)