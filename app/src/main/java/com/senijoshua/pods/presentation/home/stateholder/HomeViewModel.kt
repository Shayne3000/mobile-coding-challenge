package com.senijoshua.pods.presentation.home.stateholder

import androidx.lifecycle.ViewModel
import com.senijoshua.pods.presentation.home.model.HomePodcast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(): ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

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
