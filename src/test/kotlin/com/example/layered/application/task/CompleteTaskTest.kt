package com.example.layered.application.task

import com.example.layered.application.TaskService
import com.example.layered.model.Task
import com.example.layered.model.TaskDescription
import com.example.layered.persistence.TaskRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CompleteTaskTest {

    private val taskRepository: TaskRepository = mockk(relaxed = true)
    private val taskService = TaskService(taskRepository)

    @Test
    fun `completeTask should mark task as completed and return the updated task`() {
        // Arrange
        val taskDescription = "Task1"
        val task = Task(description = TaskDescription(taskDescription))
        every { taskRepository.allTasks } returns listOf(task)
        every { taskRepository.saveTask(any()) } returns task

        // Act
        val result = taskService.completeTask(taskDescription)

        // Assert
        assertEquals(task, result)
        assertEquals(true, result?.isCompleted)
        verify(exactly = 1) { taskRepository.saveTask(task) }
    }

    @Test
    fun `completeTask should return null when task with given description is not found`() {
        // Arrange
        val taskDescription = "NonExistentTask"
        every { taskRepository.allTasks } returns emptyList()

        // Act
        val result = taskService.completeTask(taskDescription)

        // Assert
        assertNull(result)
    }

    @Test
    fun `completeTask should throw IllegalArgumentException when description is null`() {
        // Act & Assert
        assertThrows<IllegalArgumentException> {
            taskService.completeTask(null)
        }

        // Assert that taskRepository.saveTask was not called
        verify(exactly = 0) { taskRepository.saveTask(any()) }
    }

    @Test
    fun `completeTask should throw IllegalArgumentException when description is empty`() {
        // Act & Assert
        assertThrows<IllegalArgumentException> {
            taskService.completeTask("")
        }

        // Assert that taskRepository.saveTask was not called
        verify(exactly = 0) { taskRepository.saveTask(any()) }
    }
}