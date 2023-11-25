package com.example.layered.persistence.user

import com.example.layered.model.User
import com.example.layered.model.UserName
import com.example.layered.model.UserRole
import com.example.layered.persistence.UserRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AllUsersTest {
    @Test
    fun `allUsers should return all users in the repository`() {
        // Arrange
        val userRepository = UserRepository()
        val user1 = User(userName = UserName("user1"), role = UserRole.TEAM_MEMBER)
        val user2 = User(userName = UserName("user2"), role = UserRole.MANAGER)

        // Act
        userRepository.saveUser(user1)
        userRepository.saveUser(user2)

        // Assert
        assertEquals(2, userRepository.allUsers.size)
        assertEquals(user1, userRepository.allUsers[0])
        assertEquals(user2, userRepository.allUsers[1])
    }
}