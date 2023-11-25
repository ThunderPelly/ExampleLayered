package com.example.layered.persistence.user

import com.example.layered.model.User
import com.example.layered.model.UserName
import com.example.layered.model.UserRole
import com.example.layered.persistence.UserRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class GetUserByUsernameTest {
    @Test
    fun `getUserByUsername should return user with the given username`() {
        // Arrange
        val userRepository = UserRepository()
        val username = "john.doe"
        val user = User(userName = UserName(username), role = UserRole.MANAGER)
        userRepository.saveUser(user)

        // Act
        val result = userRepository.getUserByUsername(username)

        // Assert
        assertEquals(user, result)
    }

    @Test
    fun `getUserByUsername should handle when user is not found`() {
        // Arrange
        val userRepository = UserRepository()
        val username = "nonexistent.user"

        // Act
        val result = userRepository.getUserByUsername(username)

        // Assert
        assertNull(result)
    }

}