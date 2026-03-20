package com.example.litecartesnative.features.user.presentations.viewmodel

import android.net.Uri
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
    val bio: String = "",
    val selectedImageUri: Uri? = null,
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
                        fullname = result.data.fullname,
                        bio = result.data.bio ?: ""
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

    fun onBioChanged(value: String) {
        _state.value = _state.value.copy(bio = value)
    }

    fun onImageSelected(uri: Uri?) {
        _state.value = _state.value.copy(selectedImageUri = uri)
    }

    fun saveProfile() {
        val fullname = _state.value.fullname.trim()
        if (fullname.length < 3) {
            _state.value = _state.value.copy(error = "Nama harus minimal 3 karakter")
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(isSaving = true, error = null, saveSuccess = false)

            // Upload image first if selected
            var imageUrl: String? = null
            val selectedUri = _state.value.selectedImageUri
            if (selectedUri != null) {
                when (val uploadResult = userRepository.uploadAvatar(selectedUri)) {
                    is Result.Success -> {
                        imageUrl = uploadResult.data
                    }
                    is Result.Error -> {
                        _state.value = _state.value.copy(
                            isSaving = false,
                            error = uploadResult.message
                        )
                        return@launch
                    }
                    is Result.Loading -> {}
                }
            }

            val request = UpdateProfileRequest(
                fullname = fullname,
                image = imageUrl,
                bio = _state.value.bio.trim().ifEmpty { null }
            )
            when (val result = userRepository.updateUserProfile(request)) {
                is Result.Success -> {
                    _state.value = _state.value.copy(
                        isSaving = false,
                        profile = result.data,
                        selectedImageUri = null,
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
