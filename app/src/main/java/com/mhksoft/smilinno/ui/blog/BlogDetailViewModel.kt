package com.mhksoft.smilinno.ui.blog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mhksoft.smilinno.data.model.Blog
import com.mhksoft.smilinno.data.model.Resource
import com.mhksoft.smilinno.domain.BlogUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BlogDetailScreenUiState(
    val blogDetail: Resource<Blog?> = Resource.loading(),
)

@HiltViewModel
class BlogDetailViewModel @Inject constructor(
    private val blogUseCase: BlogUseCase
) : ViewModel() {
    // UI state exposed to the UI
    private val _uiState = MutableStateFlow(BlogDetailScreenUiState())
    val uiState: StateFlow<BlogDetailScreenUiState> = _uiState.asStateFlow()

    fun getBlogDetail(id: Long) {
        viewModelScope.launch {
            blogUseCase.getBlogDetail(id).collect { blog ->
                _uiState.update {
                    it.copy(
                        blogDetail = blog
                    )
                }
            }
        }
    }
}
