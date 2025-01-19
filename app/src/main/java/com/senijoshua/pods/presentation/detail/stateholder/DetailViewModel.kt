package com.senijoshua.pods.presentation.detail.stateholder

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.senijoshua.pods.data.repository.PodcastRepository
import com.senijoshua.pods.presentation.detail.model.DetailPodcast
import com.senijoshua.pods.presentation.detail.navigation.DetailRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: PodcastRepository,
) : ViewModel() {
    private val detailRoute: DetailRoute = savedStateHandle.toRoute()
    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState

    fun getPodcast() {
        viewModelScope.launch {
            repository.getPodcastGivenId(detailRoute.podcastId).collectLatest { result ->
                when {
                    result.isSuccess -> {
                        val data = result.getOrNull()!!
                        _uiState.update { currentState ->
                            currentState.copy(
                                details = data,
                                isLoading = false,
                            )
                        }
                    }

                    result.isFailure -> {
                        val error = result.exceptionOrNull()!!
                        _uiState.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                errorMessage = error.localizedMessage
                            )
                        }
                    }
                }
            }
        }
    }

    fun toggleFavouriteStatus(podcastId: String) {
        viewModelScope.launch {
            repository.togglePodcastFavouriteStatus(podcastId)
        }
    }

    fun resetErrorState() {
        _uiState.update { currentState ->
            currentState.copy(
                errorMessage = null,
            )
        }
    }
}

/**
 * Representation of the Detail screen UI State at any instant in time.
 */
data class DetailUiState(
    val details: DetailPodcast? = null,
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
)
