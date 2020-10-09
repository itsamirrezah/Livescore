package com.itsamirrezah.livescore.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.itsamirrezah.livescore.data.models.Team
import io.reactivex.Observable

@Dao
interface TeamsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTeams(teams: List<Team>)

    @Query("select * from teams where id = :id")
    fun getTeamById(id: Int): Observable<Team>
}