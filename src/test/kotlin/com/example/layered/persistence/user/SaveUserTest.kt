package com.example.layered.persistence.user

import com.example.layered.model.User
import com.example.layered.model.UserRole
import com.example.layered.persistence.UserRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SaveUserTest {
    @Test
    fun `saveUser should add a user to the repository`() {
        // Arrange
        val userRepository = UserRepository()
        val user = User(userName = "john.doe", role = UserRole.MANAGER)

        // Act
        val result = userRepository.saveUser(user)

        // Assert
        assertEquals(user, result)
        assertEquals(1, userRepository.allUsers.size)
        assertEquals(user, userRepository.allUsers.first())
    }
}