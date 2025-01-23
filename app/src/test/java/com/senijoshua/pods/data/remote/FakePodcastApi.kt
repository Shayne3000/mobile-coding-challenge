package com.senijoshua.pods.data.remote

class FakePodcastApi : PodcastApi{
    val remotePodcastList = List(10) { index ->
        RemotePodcast(
            id = "Podcast $index id",
            title = "Podcast $index title",
            thumbnail = "Podcast $index thumbnail",
            image = "Podcast $index image",
            publisher = "Podcast $index Publisher",
            description = "Podcast $index description",
        )
    }
    val errorText = "Network Error!"
    var shouldThrowError = false

    override suspend fun getBestPodcasts(page: Int): PodcastsResponse {
        return if (shouldThrowError) {
            throw Exception(errorText)
        } else {
            PodcastsResponse(
                previousPageNumber = 1,
                podcasts = remotePodcastList,
                pageNumber = 2,
                nextPageNumber = 3
            )
        }
    }
}
