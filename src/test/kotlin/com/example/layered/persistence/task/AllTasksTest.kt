package com.example.layered.persistence.task

import com.example.layered.model.Task
import com.example.layered.model.TaskDescription
import com.example.layered.persistence.TaskRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AllTasksTest {
    @Test
    fun `allTasks should return all tasks in the repository`() {
        // Arrange
        val taskRepository = TaskRepository()
        val task1 = Task(description = TaskDescription("Task 1"))
        val task2 = Task(description = TaskDescription("Task 2"))

        // Act
        taskRepository.saveTask(task1)
        taskRepository.saveTask(task2)

        // Assert
        assertEquals(2, taskRepository.allTasks.size)
        assertEquals(task1, taskRepository.allTasks[0])
        assertEquals(task2, taskRepository.allTasks[1])
    }
}