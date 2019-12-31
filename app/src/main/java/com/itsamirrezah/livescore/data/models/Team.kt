package com.itsamirrezah.livescore.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "teams")
data class Team(

    @PrimaryKey
    private val _id: Int,
    @SerializedName("id")
    private val id: Int,
    @SerializedName("name")
    private val name: String,
    @SerializedName("shortName")
    private val shortName: String,
    @SerializedName("tla")
    private val tla: String,
    @SerializedName("crestUrl")
    private val crestUrl: String,
    @SerializedName("clubColors")
    private val clubColors: String,
    @SerializedName("venue")
    private val venue: String
)