package com.itsamirrezah.livescore.data.models

import com.google.gson.annotations.SerializedName

data class Competition(
    @SerializedName("id")
    val id: Int,
    @SerializedName("area")
    val area: Area,
    @SerializedName("name")
    val name: String,
    @SerializedName("code")
    val code: String,
    @SerializedName("emblemUrl")
    val emblemUrl: String,
    @SerializedName("plan")
    val plan: String,
    @SerializedName("numberOfAvailableSeasons")
    val numberOfAvailableSeasons: Int,
    @SerializedName("lastUpdated")
    val lastUpdated: String
)

data class Area(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)