package com.example.litecartesnative.features.user.presentations.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.litecartesnative.data.remote.dto.UpdateProfileRequest
import com.example.litecartesnative.data.remote.dto.UserProfileDto
import com.example.litecartesnative.data.repository.Result
import com.example.litecartesnative.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EditProfileState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val profile: UserProfileDto? = null,
    val fullname: String = "",
    val saveSuccess: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(EditProfileState())
    val state: StateFlow<EditProfileState> = _state.asStateFlow()

    fun loadProfile() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            when (val result = userRepository.getUserProfile()) {
                is Result.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        profile = result.data,
                        fullname = result.data.fullname
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

    fun onFullnameChanged(value: String) {
        _state.value = _state.value.copy(fullname = value)
    }

    fun saveProfile() {
        val fullname = _state.value.fullname.trim()
        if (fullname.length < 3) {
            _state.value = _state.value.copy(error = "Nama harus minimal 3 karakter")
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(isSaving = true, error = null, saveSuccess = false)
            val request = UpdateProfileRequest(fullname = fullname)
            when (val result = userRepository.updateUserProfile(request)) {
                is Result.Success -> {
                    _state.value = _state.value.copy(
                        isSaving = false,
                        profile = result.data,
                        saveSuccess = true
                    )
                }
                is Result.Error -> {
                    _state.value = _state.value.copy(
                        isSaving = false,
                        error = result.message
                    )
                }
                is Result.Loading -> {
                    _state.value = _state.value.copy(isSaving = true)
                }
            }
        }
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }

    fun clearSaveSuccess() {
        _state.value = _state.value.copy(saveSuccess = false)
    }
}
