package com.itsamirrezah.livescore.data.services

import com.itsamirrezah.livescore.data.models.CompetitionResponse
import com.itsamirrezah.livescore.data.models.MatchResponse
import com.itsamirrezah.livescore.data.models.TeamsCompResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * https://www.football-data.org/
 */
interface FootballDataApi {

    @GET("v2/matches")
    suspend fun getMatches(
        @Query("dateFrom") dateFrom: String,
        @Query("dateTo") dateTo: String,
        @Query("status") status: String,
        @Query("competitions") competitions: String
    ): MatchResponse

    @GET("v2/competitions?plan=TIER_ONE")
    suspend fun getCompetitions(): CompetitionResponse

    @GET("v2/competitions/{id}/teams")
    suspend fun getTeamsByCompetition(@Path("id") competitionId: Int): TeamsCompResponse
}