package com.example.layered.presentation.task

import com.example.layered.application.TaskService
import com.example.layered.model.Task
import com.example.layered.model.TaskDescription
import com.example.layered.presentation.TaskController
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class CompleteTaskTest {
    private val taskService: TaskService = mockk()
    private val taskController = TaskController(taskService)
    private val mockMvc: MockMvc = MockMvcBuilders.standaloneSetup(taskController).build()

    @Test
    fun `completeTask should complete the task and return the task details`() {
        // Arrange
        val taskDescription = "Existing Task"
        val completedTask = Task(description = TaskDescription(taskDescription))
        every { taskService.completeTask(taskDescription) } returns completedTask

        // Act & Assert
        performCompleteTaskRequest(taskDescription)
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.description").value(taskDescription))
    }

    @Test
    fun `completeTask should handle when the task is not found`() {
        // Arrange
        val taskDescription = "Nonexistent Task"
        every { taskService.completeTask(taskDescription) } returns null

        // Act & Assert
        performCompleteTaskRequest(taskDescription)
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.description").doesNotExist())
    }

    private fun performCompleteTaskRequest(taskDescription: String): ResultActions {
        return mockMvc.perform(
            put("/api/v1/task/{taskDescription}/complete", taskDescription)
                .contentType(MediaType.APPLICATION_JSON)
        )
    }
}