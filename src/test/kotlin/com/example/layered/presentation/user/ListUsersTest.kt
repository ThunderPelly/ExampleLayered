package com.example.layered.presentation.user

import com.example.layered.application.UserService
import com.example.layered.model.User
import com.example.layered.model.UserName
import com.example.layered.model.UserRole
import com.example.layered.presentation.UserController
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class ListUsersTest {
    private val userService: UserService = mockk()
    private val userController = UserController(userService)
    private val mockMvc: MockMvc = MockMvcBuilders.standaloneSetup(userController).build()

    @Test
    fun `listUsers should return a list of users`() {
        // Arrange
        val users = listOf(
            User(userName = UserName("john.doe"), role = UserRole.TEAM_MEMBER),
            User(userName = UserName("jane.smith"), role = UserRole.MANAGER)
        )
        every { userService.getAllUsers() } returns users

        // Act & Assert
        performListUsersRequest()
            .andExpect(jsonPath("$[0].userName").value(users[0].userName.value))
            .andExpect(jsonPath("$[0].role").value(users[0].role.name))
            .andExpect(jsonPath("$[1].userName").value(users[1].userName.value))
            .andExpect(jsonPath("$[1].role").value(users[1].role.name))
    }

    @Test
    fun `listUsers should handle an empty list of users`() {
        // Arrange
        every { userService.getAllUsers() } returns emptyList()

        // Act & Assert
        performListUsersRequest().andExpect(jsonPath("$").isEmpty)
    }

    private fun performListUsersRequest(): ResultActions {
        return mockMvc.perform(
            get("/api/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
        )
    }
}