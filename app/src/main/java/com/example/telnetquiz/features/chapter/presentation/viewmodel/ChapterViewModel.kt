package com.example.telnetquiz.features.chapter.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.telnetquiz.data.remote.dto.ChapterDetailDto
import com.example.telnetquiz.data.remote.dto.ChapterDto
import com.example.telnetquiz.data.remote.dto.QuizMaterialsResponse
import com.example.telnetquiz.data.audio.AudioManager
import com.example.telnetquiz.data.audio.SfxType
import com.example.telnetquiz.data.local.QuizFlowManager
import com.example.telnetquiz.data.repository.ChapterRepository
import com.example.telnetquiz.data.repository.QuizRepository
import com.example.telnetquiz.data.repository.Result
import com.example.telnetquiz.constants.SCORE_MIN_COMPLETE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChapterListState(
    val isLoading: Boolean = false,
    val chapters: List<ChapterDto> = emptyList(),
    val hasTakenPretest: Boolean? = null,
    val error: String? = null
)

data class ChapterDetailState(
    val isLoading: Boolean = false,
    val chapter: ChapterDetailDto? = null,
    val error: String? = null
)

data class LevelUiModel(
    val quizId: Int,
    val level: Int,
    val isCompleted: Boolean,
    val isUnlocked: Boolean,
    val score: Int?,
    val lockedMessage: String?
)

sealed class LevelNavEvent {
    data class GoToFeedback(val chapterId: Int, val level: Int, val materialId: Int) : LevelNavEvent()
    data class GoToQuiz(val quizId: Int) : LevelNavEvent()
}

@HiltViewModel
class ChapterViewModel @Inject constructor(
    private val chapterRepository: ChapterRepository,
    private val quizRepository: QuizRepository,
    private val audioManager: AudioManager,
    private val quizFlowManager: QuizFlowManager
) : ViewModel() {

    private val _listState = MutableStateFlow(ChapterListState())
    val listState: StateFlow<ChapterListState> = _listState.asStateFlow()

    private val _detailState = MutableStateFlow(ChapterDetailState())
    val detailState: StateFlow<ChapterDetailState> = _detailState.asStateFlow()

    private val _levelNavEvent = MutableSharedFlow<LevelNavEvent>()
    val levelNavEvent: SharedFlow<LevelNavEvent> = _levelNavEvent.asSharedFlow()

    private val _isFetchingMaterials = MutableStateFlow(false)
    val isFetchingMaterials: StateFlow<Boolean> = _isFetchingMaterials.asStateFlow()

    fun loadChapters() {
        viewModelScope.launch {
            _listState.value = _listState.value.copy(isLoading = true, error = null)
            when (val result = chapterRepository.getChapters()) {
                is Result.Success -> {
                    _listState.value = _listState.value.copy(
                        isLoading = false,
                        chapters = result.data.chapters,
                        hasTakenPretest = result.data.hasTakenPretest
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

    fun getLevelModels(): List<LevelUiModel> {
        val chapter = _detailState.value.chapter ?: return emptyList()
        val quizzes = chapter.quizzes
        val completedQuizIds = chapter.completedQuizIds
        val quizScores = chapter.quizScores

        return quizzes.mapIndexed { index, quiz ->
            val isCompleted = quiz.id in completedQuizIds
            val prevQuizScore = if (index > 0) quizScores[quizzes[index - 1].id.toString()] ?: 0 else 0
            val isUnlocked = index == 0 ||
                (quizzes[index - 1].id in completedQuizIds && prevQuizScore > SCORE_MIN_COMPLETE)

            val lockedMessage = if (!isUnlocked && index > 0) {
                val prevQuiz = quizzes[index - 1]
                val prevScore = quizScores[prevQuiz.id.toString()] ?: 0
                if (prevQuiz.id !in completedQuizIds) {
                    "Selesaikan Level ${prevQuiz.level} terlebih dahulu"
                } else {
                    "Kamu perlu mencapai nilai $SCORE_MIN_COMPLETE di Level ${prevQuiz.level} untuk melanjutkan. Nilai terbaikmu saat ini adalah $prevScore. Yuk, coba lagi! "
                }
            } else null

            LevelUiModel(
                quizId = quiz.id,
                level = quiz.level,
                isCompleted = isCompleted,
                isUnlocked = isUnlocked,
                score = quizScores[quiz.id.toString()],
                lockedMessage = lockedMessage
            )
        }
    }

    fun startLearnFirst(quizId: Int, chapterId: Int, level: Int) {
        viewModelScope.launch {
            _isFetchingMaterials.value = true
            when (val result = quizRepository.getQuizMaterials(quizId)) {
                is Result.Success -> {
                    val materials = result.data.materials
                    if (materials.isNotEmpty()) {
                        quizFlowManager.setupLearnFirst(
                            quizId = quizId,
                            chapterId = chapterId,
                            level = level,
                            materials = materials
                        )
                        val firstMaterial = quizFlowManager.nextMaterial()!!
                        _isFetchingMaterials.value = false
                        _levelNavEvent.emit(LevelNavEvent.GoToFeedback(chapterId, level, firstMaterial.id))
                    } else {
                        _isFetchingMaterials.value = false
                        _levelNavEvent.emit(LevelNavEvent.GoToQuiz(quizId))
                    }
                }
                is Result.Error -> {
                    _isFetchingMaterials.value = false
                    _levelNavEvent.emit(LevelNavEvent.GoToQuiz(quizId))
                }
                is Result.Loading -> {}
            }
        }
    }

    fun playDirectly(quizId: Int) {
        audioManager.playSfx(SfxType.START_LEVEL)
        viewModelScope.launch {
            _levelNavEvent.emit(LevelNavEvent.GoToQuiz(quizId))
        }
    }

    fun clearError() {
        _listState.value = _listState.value.copy(error = null)
        _detailState.value = _detailState.value.copy(error = null)
    }

    suspend fun fetchQuizMaterials(quizId: Int): Result<QuizMaterialsResponse> {
        return quizRepository.getQuizMaterials(quizId)
    }
}
