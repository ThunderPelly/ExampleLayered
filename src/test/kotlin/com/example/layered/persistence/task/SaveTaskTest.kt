package com.example.layered.persistence.task

import com.example.layered.model.Task
import com.example.layered.persistence.TaskRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SaveTaskTest {
    @Test
    fun `saveTask should add a task to the repository`() {
        // Arrange
        val taskRepository = TaskRepository()
        val task = Task(description = "New Task")

        // Act
        val result = taskRepository.saveTask(task)

        // Assert
        assertEquals(task, result)
        assertEquals(1, taskRepository.allTasks.size)
        assertEquals(task, taskRepository.allTasks.first())
    }
}