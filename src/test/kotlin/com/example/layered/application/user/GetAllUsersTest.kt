package com.example.layered.application.user

import com.example.layered.application.UserService
import com.example.layered.model.User
import com.example.layered.model.UserName
import com.example.layered.model.UserRole
import com.example.layered.persistence.UserRepository
import io.mockk.MockKAnnotations
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetAllUsersTest {

    @MockK
    private var userRepository: UserRepository = mockk<UserRepository>()

    @InjectMockKs
    private var userService: UserService = UserService(userRepository)

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        clearMocks(userRepository)
    }

    @Test
    fun `getAllUsers should return empty list when no users exist`() {
        // Arrange
        every { userRepository.allUsers } returns emptyList()

        // Act
        val result = userService.getAllUsers()

        // Assert
        assertNotNull(result)
        result?.let { assertTrue(it.isEmpty()) }
    }

    @Test
    fun `getAllUsers should return a list of users when users exist`() {
        // Arrange
        val userList =
            listOf(
                User(userName = UserName("user1"), role = UserRole.TEAM_MEMBER),
                User(userName = UserName("user2"), role = UserRole.MANAGER)
            )
        every { userRepository.allUsers } returns userList

        // Act
        val result = userService.getAllUsers()

        // Assert
        assertNotNull(result)
        result?.let { assertEquals(userList.size, it.size) }
        assertEquals(userList, result)
    }
}