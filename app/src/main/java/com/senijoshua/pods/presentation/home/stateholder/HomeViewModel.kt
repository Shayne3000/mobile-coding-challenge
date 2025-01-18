package com.senijoshua.pods.presentation.home.stateholder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senijoshua.pods.data.repository.PodcastRepository
import com.senijoshua.pods.presentation.home.model.HomePodcast
import com.senijoshua.pods.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: PodcastRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    private var page = Constants.INITIAL_PAGE

    fun getPagedPodcasts() {
        if (page > 1) {
            _uiState.update { currentState ->
                currentState.copy(isPaging = true)
            }
        }

        viewModelScope.launch {
            repository.getPodcasts(page).collectLatest { result ->
                when {
                    result.isSuccess -> {
                        val data = result.getOrNull()!!
                        processPodcastData(data)
                    }

                    result.isFailure -> {
                        val error = result.exceptionOrNull()!!
                        _uiState.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                isPaging = false,
                                isRefreshing = false,
                                errorMessage = error.localizedMessage
                            )
                        }
                    }
                }
            }
        }
    }

    private fun processPodcastData(data: List<HomePodcast>) {
        _uiState.update { currentState ->
            val updatedPodcasts =
                if (page == Constants.INITIAL_PAGE) data else currentState.podcasts + data

            currentState.copy(
                podcasts = updatedPodcasts,
                isLoading = false,
                isRefreshing = false,
                isPaging = false,
                hasPagedData = data.size == Constants.MAX_PODCASTS_PER_PAGE,
                errorMessage = null
            ).also {
                if (it.hasPagedData) page++
            }
        }
    }

    fun refreshPagedPodcasts() {
        page = Constants.INITIAL_PAGE
        _uiState.update { currentState ->
            currentState.copy(
                isRefreshing = true,
                hasPagedData = true,
            )
        }
        getPagedPodcasts()
    }

    fun onResetErrorState() {
        _uiState.update { currentState ->
            currentState.copy(errorMessage = null)
        }
    }
}

/**
 * Representation of the [HomeScreen] ui state at any instant in time.
 *
 * @param podcasts denotes the list of paged [HomePodcast] data from the DB.
 * @param isLoading denotes the first load of paged data on screen entry.
 * @param isRefreshing denotes the user-triggered fresh loads of paged data.
 * @param isPaging denotes when we're loading additional paged data from the
 * DB for appending to [podcasts].
 *  @param hasPagedData signals whether there's still paged data to load from the DB.
 *  @param errorMessage denotes details of errors that occurs whilst loading paged
 *  [HomePodcast] data for the first time OR when appending to data loaded prior.
 */
data class HomeUiState(
    val podcasts: List<HomePodcast> = emptyList(),
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val isPaging: Boolean = false,
    val hasPagedData: Boolean = true,
    val errorMessage: String? = null,
)
