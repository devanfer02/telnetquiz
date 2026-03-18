package com.example.litecartesnative.features.user.presentations.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.litecartesnative.data.remote.dto.LeaderboardEntryDto
import com.example.litecartesnative.data.remote.dto.LeaderboardUserDto
import com.example.litecartesnative.data.repository.Result
import com.example.litecartesnative.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

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
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LeaderboardState())
    val state: StateFlow<LeaderboardState> = _state.asStateFlow()

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
