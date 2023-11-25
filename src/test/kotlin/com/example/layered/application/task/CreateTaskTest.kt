package com.example.layered.application.task

import com.example.layered.application.TaskService
import com.example.layered.persistence.TaskRepository
import io.mockk.Called
import io.mockk.clearMocks
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CreateTaskTest {

    private val taskRepository: TaskRepository = mockk(relaxed = true)
    private val taskService = TaskService(taskRepository)

    @BeforeEach
    fun setUp() {
        clearMocks(taskRepository)
    }

    @Test
    fun `createTask should save task when description is valid`() {
        // Arrange
        val description = "Task Description"

        // Act
        taskService.createTask(description)

        // Assert
        verify(exactly = 1) { taskRepository.saveTask(match { it.description == description }) }
    }

    @Test
    fun `createTask should throw IllegalArgumentException when description is blank`() {
        // Arrange
        val description = ""

        // Act & Assert
        assertThrows<IllegalArgumentException> {
            taskService.createTask(description)
        }

        // Assert that taskRepository.saveTask was not called
        verify { taskRepository wasNot Called }
    }

    @Test
    fun `createTask should throw IllegalArgumentException when description is null`() {
        // Arrange
        val description: String? = null

        // Act & Assert
        assertThrows<IllegalArgumentException> {
            taskService.createTask(description)
        }

        // Assert that taskRepository.saveTask was not called
        verify { taskRepository wasNot Called }
    }
}