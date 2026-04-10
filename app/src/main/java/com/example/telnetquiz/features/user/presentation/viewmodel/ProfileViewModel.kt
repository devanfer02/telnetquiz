package com.example.telnetquiz.features.user.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.telnetquiz.constants.AvatarConstants
import com.example.telnetquiz.data.local.AvatarPreferenceManager
import com.example.telnetquiz.data.remote.dto.UserProfileDto
import com.example.telnetquiz.data.audio.AudioManager
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

data class ProfileState(
    val isLoading: Boolean = false,
    val profile: UserProfileDto? = null,
    val error: String? = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    val audioManager: AudioManager,
    private val avatarPreferenceManager: AvatarPreferenceManager
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    private val _selectedAvatarIndex = MutableStateFlow(-1)
    val selectedAvatarIndex: StateFlow<Int> = _selectedAvatarIndex.asStateFlow()

    val tag: StateFlow<String> = _state.map { state ->
        val completed = state.profile?.stats?.chaptersCompleted ?: 0
        val total = state.profile?.stats?.totalChapters ?: 0
        if (total == 0) "Penjelajah"
        else {
            val pct = (completed.toDouble() / total) * 100
            when {
                pct >= 90 -> "Legenda"
                pct >= 50 -> "Veteran"
                pct >= 25 -> "Amatir"
                else -> "Penjelajah"
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "Penjelajah")

    init {
        viewModelScope.launch {
            avatarPreferenceManager.selectedAvatarIndex.collect { index ->
                _selectedAvatarIndex.value = index
            }
        }
    }

    fun loadProfile() {
        viewModelScope.launch {
            val hasCached = _state.value.profile != null
            if (!hasCached) {
                _state.value = _state.value.copy(isLoading = true, error = null)
            }
            when (val result = userRepository.getUserProfile()) {
                is Result.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        profile = result.data
                    )
                    if (_selectedAvatarIndex.value == -1) {
                        val index = AvatarConstants.getRandomAvatarIndex(result.data.gender)
                        avatarPreferenceManager.setSelectedAvatarIndex(index)
                    }
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

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
}
