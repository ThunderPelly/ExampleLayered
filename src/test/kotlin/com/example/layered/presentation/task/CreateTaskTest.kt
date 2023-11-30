package com.example.layered.presentation.task

import com.example.layered.application.TaskService
import com.example.layered.model.Task
import com.example.layered.model.TaskDescription
import com.example.layered.presentation.TaskController
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

class CreateTaskTest {

    private val taskService: TaskService = mockk()
    private val taskController = TaskController(taskService)
    private val mockMvc: MockMvc = MockMvcBuilders.standaloneSetup(taskController).build()

    @Test
    fun `createTask should return 200 when a task is created successfully`() {
        // Arrange
        val taskDescription = "New Task"
        every { taskService.createTask(taskDescription) } returns Task(description = TaskDescription(taskDescription))

        // Act & Assert
        performCreateTaskRequest(TaskRequestDto(taskDescription))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.description").value(taskDescription))
    }

    @Test
    fun `createTask should handle when the task description is blank`() {
        // Arrange
        val taskDescription = ""
        every { taskService.createTask(taskDescription) } returns null

        // Act & Assert
        performCreateTaskRequest(TaskRequestDto(taskDescription))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.description").doesNotExist())
    }

    @Test
    fun `createTask should handle when the task description is null`() {
        // Arrange
        val taskDescription: String? = null
        every { taskService.createTask(taskDescription) } returns null

        // Act & Assert
        performCreateTaskRequest(TaskRequestDto(taskDescription))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.description").doesNotExist())
    }

    private fun performCreateTaskRequest(taskRequestDto: TaskRequestDto): ResultActions {
        return mockMvc.perform(
            post("/api/v1/task")
                .content(ObjectMapper().writeValueAsString(taskRequestDto))
                .contentType(MediaType.APPLICATION_JSON)
        )
    }
}