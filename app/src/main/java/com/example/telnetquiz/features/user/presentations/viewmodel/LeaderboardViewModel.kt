package com.example.telnetquiz.features.user.presentations.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.telnetquiz.constants.AvatarConstants
import com.example.telnetquiz.data.local.AvatarPreferenceManager
import com.example.telnetquiz.data.remote.dto.DayActivityDto
import com.example.telnetquiz.data.remote.dto.LeaderboardEntryDto
import com.example.telnetquiz.data.remote.dto.LeaderboardUserDto
import com.example.telnetquiz.data.repository.Result
import com.example.telnetquiz.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class LeaderboardTab {
    PROGRESS, LEADERBOARD
}

data class ActivityState(
    val isLoading: Boolean = false,
    val days: List<DayActivityDto> = emptyList(),
    val error: String? = null
)

data class LeaderboardState(
    val isLoading: Boolean = false,
    val leaderboard: List<LeaderboardEntryDto> = emptyList(),
    val currentUser: LeaderboardUserDto? = null,
    val hasNextPage: Boolean = false,
    val nextCursor: Int? = null,
    val error: String? = null
)

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val userRepository: UserRepository,
    avatarPreferenceManager: AvatarPreferenceManager
) : ViewModel() {

    val localAvatarResId: StateFlow<Int?> = avatarPreferenceManager.selectedAvatarIndex
        .map { index -> AvatarConstants.getAvatarResId(index) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _selectedTab = MutableStateFlow(LeaderboardTab.PROGRESS)
    val selectedTab: StateFlow<LeaderboardTab> = _selectedTab.asStateFlow()

    private val _activityState = MutableStateFlow(ActivityState())
    val activityState: StateFlow<ActivityState> = _activityState.asStateFlow()

    private val _state = MutableStateFlow(LeaderboardState())
    val state: StateFlow<LeaderboardState> = _state.asStateFlow()

    private var progressLoaded = false
    private var leaderboardLoaded = false

    fun selectTab(tab: LeaderboardTab) {
        _selectedTab.value = tab
        when (tab) {
            LeaderboardTab.PROGRESS -> if (!progressLoaded) loadRecentActivity()
            LeaderboardTab.LEADERBOARD -> if (!leaderboardLoaded) loadLeaderboard()
        }
    }

    fun loadRecentActivity() {
        viewModelScope.launch {
            _activityState.value = _activityState.value.copy(isLoading = true, error = null)
            when (val result = userRepository.getRecentActivity()) {
                is Result.Success -> {
                    _activityState.value = _activityState.value.copy(
                        isLoading = false,
                        days = result.data.activities
                    )
                    progressLoaded = true
                }
                is Result.Error -> {
                    _activityState.value = _activityState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                is Result.Loading -> {
                    _activityState.value = _activityState.value.copy(isLoading = true)
                }
            }
        }
    }

    fun loadLeaderboard() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            when (val result = userRepository.getLeaderboard()) {
                is Result.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        leaderboard = result.data.leaderboard,
                        currentUser = result.data.currentUser,
                        hasNextPage = result.data.pagination.hasNextPage,
                        nextCursor = result.data.pagination.nextCursor
                    )
                    leaderboardLoaded = true
                }
                is Result.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                is Result.Loading -> {
                    _state.value = _state.value.copy(isLoading = true)
                }
            }
        }
    }

    fun loadMore() {
        val cursor = _state.value.nextCursor ?: return
        if (!_state.value.hasNextPage) return

        viewModelScope.launch {
            when (val result = userRepository.getLeaderboard(cursor = cursor)) {
                is Result.Success -> {
                    _state.value = _state.value.copy(
                        leaderboard = _state.value.leaderboard + result.data.leaderboard,
                        currentUser = result.data.currentUser,
                        hasNextPage = result.data.pagination.hasNextPage,
                        nextCursor = result.data.pagination.nextCursor
                    )
                }
                is Result.Error -> {
                    _state.value = _state.value.copy(error = result.message)
                }
                is Result.Loading -> {}
            }
        }
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
}
