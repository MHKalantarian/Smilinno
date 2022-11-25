package com.mhksoft.smilinno.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mhksoft.smilinno.data.model.Blog
import com.mhksoft.smilinno.data.model.Resource
import com.mhksoft.smilinno.data.model.Slider
import com.mhksoft.smilinno.domain.HomeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeScreenUiState(
    val sliders: Resource<List<Slider>?> = Resource.loading(),
    val popularBlogs: Resource<List<Blog>?> = Resource.loading(),
    val latestBlogs: Resource<List<Blog>?> = Resource.loading()
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeUseCase: HomeUseCase
) : ViewModel() {
    init {
        getSliders()
        getPopularBlogs()
        getLatestBlogs()
    }

    // UI state exposed to the UI
    private val _uiState = MutableStateFlow(HomeScreenUiState())
    val uiState: StateFlow<HomeScreenUiState> = _uiState.asStateFlow()

    fun getSliders() {
        viewModelScope.launch {
            homeUseCase.getSliders().collect { sliders ->
                _uiState.update {
                    it.copy(
                        sliders = sliders
                    )
                }
            }
        }
    }

    fun getPopularBlogs() {
        viewModelScope.launch {
            homeUseCase.getPopularBlogs().collect { blogs ->
                _uiState.update {
                    it.copy(
                        popularBlogs = blogs
                    )
                }
            }
        }
    }

    fun getLatestBlogs() {
        viewModelScope.launch {
            homeUseCase.getLatestBlogs().collect { blogs ->
                _uiState.update {
                    it.copy(
                        latestBlogs = blogs
                    )
                }
            }
        }
    }
}
