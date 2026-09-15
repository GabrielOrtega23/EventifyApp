package com.gabriel.eventify.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gabriel.eventify.data.local.entity.UserEntity
import com.gabriel.eventify.data.repository.AuthRepository
import com.gabriel.eventify.data.repository.AuthResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val loggedInUser: UserEntity? = null
)

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Preencha e-mail e senha.")
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                when (val result = repository.login(email, password)) {
                    is AuthResult.Success -> _uiState.value =
                        _uiState.value.copy(isLoading = false, loggedInUser = result.user)

                    is AuthResult.Error -> _uiState.value =
                        _uiState.value.copy(isLoading = false, errorMessage = result.message)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Erro ao entrar: ${e.message ?: "tente novamente"}"
                )
            }
        }
    }

    fun signUp(name: String, email: String, password: String, confirmPassword: String) {
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Preencha todos os campos.")
            return
        }
        if (password != confirmPassword) {
            _uiState.value = _uiState.value.copy(errorMessage = "As senhas não coincidem.")
            return
        }
        if (password.length < 6) {
            _uiState.value = _uiState.value.copy(errorMessage = "A senha deve ter ao menos 6 caracteres.")
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                when (val result = repository.signUp(name, email, password)) {
                    is AuthResult.Success -> _uiState.value =
                        _uiState.value.copy(isLoading = false, loggedInUser = result.user)

                    is AuthResult.Error -> _uiState.value =
                        _uiState.value.copy(isLoading = false, errorMessage = result.message)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Erro ao criar conta: ${e.message ?: "tente novamente"}"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}
