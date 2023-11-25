package com.example.layered.presentation.user

import com.example.layered.application.UserService
import com.example.layered.model.User
import com.example.layered.model.UserRole
import com.example.layered.presentation.UserController
import com.example.layered.presentation.dto.UserRequestDto
import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class CreateUserTest {

    private val userService: UserService = mockk()
    private val userController = UserController(userService)
    private val mockMvc: MockMvc = MockMvcBuilders.standaloneSetup(userController).build()

    @Test
    fun `createUser should return created user`() {
        // Arrange
        val userName = "john."
        val userRole = UserRole.TEAM_MEMBER
        every { userService.createUser(userName, userRole) } returns
                User(userName = "john.", role = userRole)

        val requestDto = UserRequestDto(userName, userRole)
        val requestBody = ObjectMapper().writeValueAsString(requestDto)

        // Act & Assert
        performCreateUserRequest(requestBody)
            .andExpect(jsonPath("$.userName").value(userName))
            .andExpect(jsonPath("$.role").value(userRole.name))
    }

    @Test
    fun `createUser should handle null user response`() {
        // Arrange
        val userName = "john.doe"
        val userRole = UserRole.TEAM_MEMBER
        every { userService.createUser(userName, userRole) } returns null

        val requestDto = UserRequestDto(userName, userRole)
        val requestBody = ObjectMapper().writeValueAsString(requestDto)

        // Act & Assert
        performCreateUserRequest(requestBody)
            .andExpect(jsonPath("$.userName").doesNotExist())
            .andExpect(jsonPath("$.role").doesNotExist())
    }

    private fun performCreateUserRequest(requestBody: String): ResultActions {
        return mockMvc.perform(
            post("/api/v1/user")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
        )
    }
}