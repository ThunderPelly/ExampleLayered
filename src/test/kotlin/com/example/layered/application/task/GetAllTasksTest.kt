package com.example.layered.application.task

import com.example.layered.application.TaskService
import com.example.layered.model.Task
import com.example.layered.persistence.TaskRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GetAllTasksTest {
    private val taskRepository: TaskRepository = mockk(relaxed = true)
    private val taskService = TaskService(taskRepository)

    @Test
    fun `getAllTasks should return empty list when no tasks are present`() {
        // Arrange
        val emptyTaskList = emptyList<Task>()
        every { taskRepository.allTasks } returns emptyTaskList

        // Act
        val result = taskService.getAllTasks()

        // Assert
        assertEquals(emptyTaskList, result)
    }

    @Test
    fun `getAllTasks should return a list of tasks when tasks are present`() {
        // Arrange
        val tasks = listOf(Task(description = "Task1"), Task(description = "Task2"))
        every { taskRepository.allTasks } returns tasks

        // Act
        val result = taskService.getAllTasks()

        // Assert
        assertEquals(tasks, result)
    }
}