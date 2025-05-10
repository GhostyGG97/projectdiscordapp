package com.example.discordcloneapp.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.discordcloneapp.data.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val repo: AuthRepository = AuthRepository()) : ViewModel() {
    private val _isLoggedIn = MutableStateFlow(repo.isUserLoggedIn())
    val isLoggedIn = _isLoggedIn.asStateFlow()

    fun login(email: String, password: String) = viewModelScope.launch {
        repo.login(email, password).onSuccess {
            _isLoggedIn.value = true
        }.onFailure {
            // Manejo de error
        }
    }

    fun register(email: String, password: String) = viewModelScope.launch {
        repo.register(email, password).onSuccess {
            _isLoggedIn.value = true
        }
    }

    fun logout() {
        repo.logout()
        _isLoggedIn.value = false
    }
}