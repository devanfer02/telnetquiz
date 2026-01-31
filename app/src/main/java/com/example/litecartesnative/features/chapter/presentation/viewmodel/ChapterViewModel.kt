package com.example.litecartesnative.features.chapter.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.litecartesnative.data.remote.dto.ChapterDetailDto
import com.example.litecartesnative.data.remote.dto.ChapterDto
import com.example.litecartesnative.data.repository.ChapterRepository
import com.example.litecartesnative.data.repository.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChapterListState(
    val isLoading: Boolean = false,
    val chapters: List<ChapterDto> = emptyList(),
    val error: String? = null
)

data class ChapterDetailState(
    val isLoading: Boolean = false,
    val chapter: ChapterDetailDto? = null,
    val error: String? = null
)

@HiltViewModel
class ChapterViewModel @Inject constructor(
    private val chapterRepository: ChapterRepository
) : ViewModel() {

    private val _listState = MutableStateFlow(ChapterListState())
    val listState: StateFlow<ChapterListState> = _listState.asStateFlow()

    private val _detailState = MutableStateFlow(ChapterDetailState())
    val detailState: StateFlow<ChapterDetailState> = _detailState.asStateFlow()

    fun loadChapters() {
        viewModelScope.launch {
            _listState.value = _listState.value.copy(isLoading = true, error = null)
            when (val result = chapterRepository.getChapters()) {
                is Result.Success -> {
                    _listState.value = _listState.value.copy(
                        isLoading = false,
                        chapters = result.data
                    )
                }
                is Result.Error -> {
                    _listState.value = _listState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                is Result.Loading -> {
                    _listState.value = _listState.value.copy(isLoading = true)
                }
            }
        }
    }

    fun loadChapterById(id: Int) {
        viewModelScope.launch {
            _detailState.value = _detailState.value.copy(isLoading = true, error = null)
            when (val result = chapterRepository.getChapterById(id)) {
                is Result.Success -> {
                    _detailState.value = _detailState.value.copy(
                        isLoading = false,
                        chapter = result.data
                    )
                }
                is Result.Error -> {
                    _detailState.value = _detailState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                is Result.Loading -> {
                    _detailState.value = _detailState.value.copy(isLoading = true)
                }
            }
        }
    }

    fun clearError() {
        _listState.value = _listState.value.copy(error = null)
        _detailState.value = _detailState.value.copy(error = null)
    }
}
