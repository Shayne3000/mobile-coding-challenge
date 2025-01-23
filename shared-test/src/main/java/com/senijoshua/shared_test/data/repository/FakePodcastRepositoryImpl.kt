package com.senijoshua.shared_test.data.repository

import androidx.paging.PagingData
import com.senijoshua.pods.data.repository.PodcastRepository
import com.senijoshua.pods.presentation.detail.model.DetailPodcast
import com.senijoshua.pods.presentation.detail.model.fakeDetailPodcastArticle
import com.senijoshua.pods.presentation.home.model.HomePodcast
import com.senijoshua.pods.presentation.home.model.fakePodcastList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Fake implementation of the [PodcastRepository] for testing purposes.
 */
class FakePodcastRepositoryImpl @Inject constructor(): PodcastRepository {
    private var podcastStore: Podcast = Podcast(
        id = fakeDetailPodcastArticle.id,
        title = fakeDetailPodcastArticle.title,
        thumbnail = fakePodcastList[0].thumbnail,
        image = fakeDetailPodcastArticle.image,
        publisher = fakeDetailPodcastArticle.publisher,
        description = fakeDetailPodcastArticle.description,
        isFavourite = fakeDetailPodcastArticle.isFavourite,
    )
    var shouldThrowError = false
    var errorText = "Error!"

    override suspend fun getPagedPodcasts(): Flow<PagingData<HomePodcast>> = flow {
        emit(
            if (shouldThrowError) {
                PagingData.empty()
            } else {
                PagingData.from(fakePodcastList)
            }
        )
    }

    override suspend fun getPodcastGivenId(podcastId: String): Flow<Result<DetailPodcast>> = flow {
        if (shouldThrowError) {
            emit(Result.failure(Throwable(errorText)))
        } else {
            emit(Result.success(podcastStore.toDetailPodcast()))
        }
    }

    override suspend fun togglePodcastFavouriteStatus(podcastId: String) {
        podcastStore.isFavourite = !podcastStore.isFavourite
    }
}

private data class Podcast(
    val id: String,
    val title: String,
    val thumbnail: String,
    val image: String,
    val publisher: String,
    val description: String,
    var isFavourite: Boolean = false,
)

private fun Podcast.toDetailPodcast() = DetailPodcast(
    id = id,
    title = title,
    image = image,
    publisher = publisher,
    description = description,
    isFavourite = isFavourite
)
