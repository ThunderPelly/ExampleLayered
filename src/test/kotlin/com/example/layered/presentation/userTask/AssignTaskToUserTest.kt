package com.example.layered.presentation.userTask


import com.example.layered.application.UserTaskService
import com.example.layered.model.Task
import com.example.layered.presentation.UserTaskController
import com.example.layered.presentation.dto.TaskRequestDto
import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class AssignTaskToUserTest {

    private val userTaskService: UserTaskService = mockk()
    private val taskController = UserTaskController(userTaskService)
    private val mockMvc: MockMvc = MockMvcBuilders.standaloneSetup(taskController).build()

    @Test
    fun `assignTaskToUser should return updated Task when a task is assigned successfully`() {
        // Arrange
        val userName = "john.doe"
        val taskDescription = "New Task"
        every {
            userTaskService.assignTaskToUser(
                userName,
                taskDescription
            )
        } returns Task(description = taskDescription)

        // Act & Assert
        performAssignTaskToUserRequest(userName, TaskRequestDto(taskDescription))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.description").value(taskDescription))
    }

    @Test
    fun `assignTaskToUser should handle when the task description is blank`() {
        // Arrange
        val userName = "john.doe"
        val taskDescription = ""
        every { userTaskService.assignTaskToUser(userName, taskDescription) } returns null

        // Act & Assert
        performAssignTaskToUserRequest(userName, TaskRequestDto(taskDescription))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.description").doesNotExist())
    }

    @Test
    fun `assignTaskToUser should handle when the task description is null`() {
        // Arrange
        val userName = "john.doe"
        val taskDescription: String? = null
        every { userTaskService.assignTaskToUser(userName, taskDescription) } returns null

        // Act & Assert
        performAssignTaskToUserRequest(userName, TaskRequestDto(taskDescription))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.description").doesNotExist())

    }

    private fun performAssignTaskToUserRequest(userName: String, taskRequestDto: TaskRequestDto): ResultActions {
        return mockMvc.perform(
            post("/api/v1/user-task")
                .param("userName", userName)
                .content(ObjectMapper().writeValueAsString(taskRequestDto))
                .contentType(MediaType.APPLICATION_JSON)
        )
    }
}