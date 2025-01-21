package com.senijoshua.pods.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Abstraction for interacting with the remote service
 */
interface PodcastApi {
    @GET("best_podcasts")
    suspend fun getBestPodcasts(
        @Query("page") page: Int,
    ): PodcastsResponse
}
