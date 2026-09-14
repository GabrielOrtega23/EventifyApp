package com.gabriel.eventify.data.repository

import com.gabriel.eventify.data.local.dao.UserDao
import com.gabriel.eventify.data.local.entity.UserEntity

sealed class AuthResult {
    data class Success(val user: UserEntity) : AuthResult()
    data class Error(val message: String) : AuthResult()
}

class AuthRepository(private val userDao: UserDao) {

    suspend fun login(email: String, password: String): AuthResult {
        val user = userDao.login(email.trim(), password)
        return if (user != null) {
            AuthResult.Success(user)
        } else {
            AuthResult.Error("E-mail ou senha inválidos.")
        }
    }

    suspend fun signUp(name: String, email: String, password: String): AuthResult {
        val existing = userDao.getUserByEmail(email.trim())
        if (existing != null) {
            return AuthResult.Error("Já existe uma conta com este e-mail.")
        }
        val newUser = UserEntity(name = name.trim(), email = email.trim(), password = password)
        val id = userDao.insertUser(newUser)
        return AuthResult.Success(newUser.copy(id = id.toInt()))
    }
}
