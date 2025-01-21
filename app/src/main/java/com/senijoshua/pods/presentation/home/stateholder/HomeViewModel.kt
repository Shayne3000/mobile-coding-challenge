package com.senijoshua.pods.presentation.home.stateholder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.senijoshua.pods.data.repository.PodcastRepository
import com.senijoshua.pods.presentation.home.model.HomePodcast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: PodcastRepository) : ViewModel() {
    val pagedPodcasts: Flow<PagingData<HomePodcast>> by lazy {
        flow {
            emitAll(
                repository.getPagedPodcasts()
            )
        }.cachedIn(viewModelScope)
    }
}

