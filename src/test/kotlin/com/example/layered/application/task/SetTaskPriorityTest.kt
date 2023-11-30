package com.example.layered.application.task

import com.example.layered.application.TaskService
import com.example.layered.model.Task
import com.example.layered.model.TaskDescription
import com.example.layered.persistence.TaskRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows


class SetTaskPriorityTest {
    private val taskRepository: TaskRepository = mockk(relaxed = true)
    private val taskService = TaskService(taskRepository)

    @Test
    fun `setTaskPriority should update task priority and return the updated task`() {
        // Arrange
        val taskDescription = "Task1"
        val updatedPriority = 3
        val task = Task(description = TaskDescription(taskDescription))
        every { taskRepository.allTasks } returns listOf(task)
        every { taskRepository.saveTask(any()) } returns task

        // Act
        val result = taskService.setTaskPriority(taskDescription, updatedPriority)

        // Assert
        assertEquals(task, result)
        result?.priority?.let { assertEquals(updatedPriority, it.value) }
        verify(exactly = 1) { taskRepository.saveTask(task) }
    }

    @Test
    fun `setTaskPriority should return null when task with given description is not found`() {
        // Arrange
        val taskDescription = "NonExistentTask"
        val priority = 2
        every { taskRepository.allTasks } returns emptyList()

        // Act
        val result = taskService.setTaskPriority(taskDescription, priority)

        // Assert
        assertNull(result)
    }

    @Test
    fun `setTaskPriority should throw IllegalArgumentException when description is null`() {
        // Act & Assert
        assertThrows<IllegalArgumentException> {
            taskService.setTaskPriority(null, 0)
        }

        // Assert that taskRepository.saveTask was not called
        verify(exactly = 0) { taskRepository.saveTask(any()) }
    }

    @Test
    fun `setTaskPriority should throw IllegalArgumentException when description is empty`() {
        // Act & Assert
        assertThrows<IllegalArgumentException> {
            taskService.setTaskPriority("", 0)
        }

        // Assert that taskRepository.saveTask was not called
        verify(exactly = 0) { taskRepository.saveTask(any()) }
    }

    @Test
    fun `setTaskPriority should throw IllegalArgumentException when priority is negative`() {
        // Arrange
        val taskDescription = "Task2"

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            taskService.setTaskPriority(taskDescription, -1)
        }
        verify(exactly = 0) { taskRepository.saveTask(any()) }
    }

    @Test
    fun `setTaskPriority should throw IllegalArgumentException when priority is null`() {
        // Arrange
        val taskDescription = "Task2"
        val priority = null

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            taskService.setTaskPriority(taskDescription, priority)
        }
        verify(exactly = 0) { taskRepository.saveTask(any()) }
    }
}