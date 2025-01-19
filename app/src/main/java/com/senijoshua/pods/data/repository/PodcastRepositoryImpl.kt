package com.senijoshua.pods.data.repository

import com.senijoshua.pods.data.local.PodcastDao
import com.senijoshua.pods.data.remote.PodcastApi
import com.senijoshua.pods.util.GlobalConstants
import com.senijoshua.pods.data.util.asResult
import com.senijoshua.pods.data.util.toDetailPodcast
import com.senijoshua.pods.data.util.toHomePodcast
import com.senijoshua.pods.data.util.toLocal
import com.senijoshua.pods.presentation.detail.model.DetailPodcast
import com.senijoshua.pods.presentation.home.model.HomePodcast
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * [PodcastRepository] implementation that abstracts away details of data retrieval
 * from the presentation layer and retrieves paged podcast data in an offline-first
 * manner.
 */
class PodcastRepositoryImpl @Inject constructor(
    private val local: PodcastDao,
    private val remote: PodcastApi,
    private val dispatcher: CoroutineDispatcher,
) : PodcastRepository {

    /**
     * Get the paged list of podcasts as an observable stream.
     *
     * NB: In a non-dummy endpoint scenario, we would persist the
     * retrieved page numbers in a different table and use that to retrieve
     * more data from the Podcasts table if present and barring that, the remote service.
     */
    override suspend fun getPodcasts(page: Int): Flow<Result<List<HomePodcast>>> {
        val offset = (page - GlobalConstants.INITIAL_PAGE) * GlobalConstants.MAX_PODCASTS_PER_PAGE

        return local.getAllPodcasts(limit = GlobalConstants.MAX_PODCASTS_PER_PAGE, offset = offset)
            .map { podcastEntities ->  podcastEntities.toHomePodcast() }
            .onEach { homePodcasts ->
                if (homePodcasts.isEmpty()) {
                    val remotePodcasts = remote.getBestPodcasts(page)

                    local.insertPodcasts(remotePodcasts.podcasts.toLocal())
                }
            }
            .flowOn(dispatcher)
            .asResult()
    }

    override suspend fun getPodcastGivenId(podcastId: String): Flow<Result<DetailPodcast>> {
        return local.getPodcastGivenId(podcastId).map { podcast ->
            podcast.toDetailPodcast()
        }.flowOn(dispatcher).asResult()
    }

    override suspend fun togglePodcastFavouriteStatus(podcastId: String) {
        withContext(dispatcher) {
            local.favouritePodcast(podcastId)
        }
    }

    /**
     * If the DB isn't empty and we're loading from the Initial page,
     * we're probably refreshing and as such, the DB should be cleared before
     * retrieving new data.
     */
    private suspend fun invalidateOnRefresh(page: Int) {
        withContext(dispatcher) {
            if (page == GlobalConstants.INITIAL_PAGE && local.getNumberOfPodcasts() != 0) {
                local.clearPodcasts()
            }
        }
    }
}
