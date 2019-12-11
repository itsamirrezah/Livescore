package com.itsamirrezah.livescore.data.services

import com.itsamirrezah.livescore.data.models.MatchResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * https://www.football-data.org/
 */
interface FootballDataApi {

    @GET("v2/matches")
    fun getMatches(
        @Query("dateFrom") dateFrom: String,
        @Query("dateTo") dateTo: String
    ): Single<List<MatchResponse>>
}