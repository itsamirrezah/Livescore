package com.itsamirrezah.livescore.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.itsamirrezah.livescore.data.models.Team

@Database(entities = arrayOf(Team::class), version = 1, exportSchema = false)
abstract class LivescoreDb : RoomDatabase() {

    abstract fun teamsDao(): TeamsDao

    companion object {

        @Volatile
        private var instance: LivescoreDb? = null

        fun getInstance(context: Context): LivescoreDb {
            if (instance == null)
                instance = Room.databaseBuilder(context, LivescoreDb::class.java, "db")
                    .build()
            return instance!!
        }
    }
}