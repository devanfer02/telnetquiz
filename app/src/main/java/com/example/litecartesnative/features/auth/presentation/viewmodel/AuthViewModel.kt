package com.example.litecartesnative.features.auth.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.litecartesnative.data.local.TokenManager
import com.example.litecartesnative.data.repository.AuthRepository
import com.example.litecartesnative.data.repository.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state.asStateFlow()

    private val _sessionExpiredEvent = MutableSharedFlow<Unit>()
    val sessionExpiredEvent: SharedFlow<Unit> = _sessionExpiredEvent.asSharedFlow()

    init {
        checkLoginStatus()
        observeSessionExpired()
    }

    private fun checkLoginStatus() {
        viewModelScope.launch {
            authRepository.authToken.collect { token ->
                _state.value = _state.value.copy(isLoggedIn = token != null)
            }
        }
    }

    private fun observeSessionExpired() {
        viewModelScope.launch {
            tokenManager.sessionExpired.collect {
                _sessionExpiredEvent.emit(Unit)
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            when (val result = authRepository.login(email, password)) {
                is Result.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        isLoggedIn = true,
                        successMessage = result.data
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

    fun register(fullname: String, email: String, password: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            Log.d("REGISTER", "calling API")
            when (val result = authRepository.register(fullname, email, password)) {
                is Result.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        successMessage = result.data
                    )
                    Log.d("REGISTER", "success")
                }
                is Result.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                    Log.d("REGISTER", result.message)
                }
                is Result.Loading -> {
                    _state.value = _state.value.copy(isLoading = true)
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _state.value = AuthState(isLoggedIn = false)
        }
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }

    fun clearSuccessMessage() {
        _state.value = _state.value.copy(successMessage = null)
    }
}
