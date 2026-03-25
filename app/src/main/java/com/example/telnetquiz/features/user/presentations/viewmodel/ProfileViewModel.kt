package com.example.telnetquiz.features.user.presentations.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.telnetquiz.data.remote.dto.UserProfileDto
import com.example.telnetquiz.data.repository.Result
import com.example.telnetquiz.data.repository.UserRepository
import com.example.telnetquiz.features.quiz.presentation.singletons.ProfileCache
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileState(
    val isLoading: Boolean = false,
    val profile: UserProfileDto? = null,
    val error: String? = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(
        ProfileCache.profile?.let { ProfileState(profile = it) } ?: ProfileState()
    )
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    fun loadProfile() {
        viewModelScope.launch {
            val hasCached = _state.value.profile != null
            if (!hasCached) {
                _state.value = _state.value.copy(isLoading = true, error = null)
            }
            when (val result = userRepository.getUserProfile()) {
                is Result.Success -> {
                    ProfileCache.profile = result.data
                    _state.value = _state.value.copy(
                        isLoading = false,
                        profile = result.data
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

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
}
