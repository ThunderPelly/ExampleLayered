package com.example.layered.presentation.task

import com.example.layered.application.TaskService
import com.example.layered.model.Task
import com.example.layered.presentation.TaskController
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class ListTasksTest {

    private val taskService: TaskService = mockk()
    private val taskController = TaskController(taskService)
    private val mockMvc: MockMvc = MockMvcBuilders.standaloneSetup(taskController).build()

    @Test
    fun `listTasks should return a list of task descriptions`() {
        // Arrange
        val task1 = Task(description = "Task 1")
        val task2 = Task(description = "Task 2")
        every { taskService.getAllTasks() } returns listOf(task1, task2)

        // Act & Assert
        performListTasksRequest()
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].description").value("Task 1"))
            .andExpect(jsonPath("$[1].description").value("Task 2"))
    }

    @Test
    fun `listTasks should return an empty list`() {
        // Arrange
        every { taskService.getAllTasks() } returns emptyList()

        // Act & Assert
        performListTasksRequest()
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isEmpty)
    }

    private fun performListTasksRequest(): ResultActions {
        return mockMvc.perform(
            get("/api/v1/task")
                .contentType(MediaType.APPLICATION_JSON)
        )
    }
}