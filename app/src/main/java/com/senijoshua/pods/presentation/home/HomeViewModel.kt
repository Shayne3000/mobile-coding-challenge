package com.senijoshua.pods.presentation.home

import androidx.lifecycle.ViewModel
import com.senijoshua.pods.presentation.home.model.HomePodcast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(): ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
}

/**
 * Representation of the [HomeScreen] ui state at any instant in time.
 *
 * @param podcasts denotes the list of paged [HomePodcast] data from the DB.
 * @param isLoading denotes the first load of paged data on screen entry.
 * @param isPaging denotes when we're loading additional paged data from the
 * DB for appending to [podcasts].
 *  @param hasPagedData signals whether there's still paged data to load from the DB.
 *  @param errorMessage denotes details of errors that occurs whilst loading paged
 *  [HomePodcast] data for the first time or appending to data loaded prior.
 */
data class HomeUiState(
    val podcasts: List<HomePodcast> = emptyList(),
    val isLoading: Boolean = true,
    val isPaging: Boolean = false,
    val hasPagedData: Boolean = true,
    val errorMessage: String? = null,
)
