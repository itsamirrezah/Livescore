package com.itsamirrezah.livescore.data.models

import com.google.gson.annotations.SerializedName

data class TeamsCompResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("competition")
    val competition: Competition,
    @SerializedName("teams")
    val teams: List<Team>
)