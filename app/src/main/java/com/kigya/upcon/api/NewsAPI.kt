package com.kigya.upcon.api

import com.kigya.upcon.models.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {

    @GET(APIConstants.BREAKING_NEWS_PATH)
    suspend fun getBreakingNews(
        @Query(APIConstants.COUNTRY_PARAM) counryCode: String = "us",
        @Query(APIConstants.PAGE_PARAM) page: Int = 1
    ): Response<NewsResponse>

    @GET(APIConstants.EVERYTHING_PATH)
    suspend fun searchForNews(
        @Query(APIConstants.QUERY_PARAM) query: String,
        @Query(APIConstants.PAGE_PARAM) page: Int = 1
    ): Response<NewsResponse>
}
