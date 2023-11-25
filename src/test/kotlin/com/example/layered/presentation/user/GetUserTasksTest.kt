package com.example.layered.presentation.user

import com.example.layered.application.UserService
import com.example.layered.model.Task
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

class GetUserTasksTest {
    private val userService: UserService = mockk()
    private val userController = UserController(userService)
    private val mockMvc: MockMvc = MockMvcBuilders.standaloneSetup(userController).build()

    @Test
    fun `getUserTasks should return a list of tasks for a user`() {
        // Arrange
        val username = "john.doe"
        val tasks = listOf(Task(description = "Task 1"), Task(description = "Task 2"))
        every { userService.getUserTasks(username) } returns tasks

        // Act & Assert
        performGetUserTasksRequest(username)
            .andExpect(jsonPath("$[0].description").value(tasks[0].description))
            .andExpect(jsonPath("$[1].description").value(tasks[1].description))
    }

    @Test
    fun `getUserTasks should handle an empty list of tasks for a user`() {
        // Arrange
        val username = "jane.smith"
        every { userService.getUserTasks(username) } returns emptyList()

        // Act & Assert
        performGetUserTasksRequest(username).andExpect(jsonPath("$").isEmpty)
    }

    @Test
    fun `getUserTasks should handle for a user with no tasks`() {
        // Arrange
        val username = "nonexistent.user"
        every { userService.getUserTasks(username) } returns null

        // Act & Assert
        performGetUserTasksRequest(username).andExpect(jsonPath("$").doesNotExist())
    }

    private fun performGetUserTasksRequest(username: String): ResultActions {
        return mockMvc.perform(
            get("/api/v1/user/task")
                .param("username", username)
                .contentType(MediaType.APPLICATION_JSON)
        )
    }
}