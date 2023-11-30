package com.example.layered.application.project

import com.example.layered.application.ProjectService
import com.example.layered.model.Project
import com.example.layered.model.ProjectName
import com.example.layered.persistence.ProjectRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.util.*

class AddTaskToProjectTest {
    private val projectRepository: ProjectRepository = mockk(relaxed = true)
    private val projectService = ProjectService(projectRepository)

    @Test
    fun `addTaskToProject should add a task to the project and return the updated project`() {
        // Arrange
        val projectId = UUID.randomUUID()
        val project = Project(projectId, ProjectName("Test Project"))
        val taskDescription = "New Task"
        every { projectRepository.getProjectById(projectId) } returns project

        // Act
        val result = projectService.addTaskToProject(projectId, taskDescription)

        // Assert
        assertEquals(project, result)
        assertEquals(1, result?.tasks?.size)
        assertEquals(taskDescription, result?.tasks?.first()?.description?.value)
    }

    @Test
    fun `addTaskToProject should handle when project with given id is not found`() {
        // Arrange
        val projectId = UUID.randomUUID()
        every { projectRepository.getProjectById(projectId) } returns null

        // Act & Assert
        assertEquals(null, projectService.addTaskToProject(projectId, "Test"))
    }

    @Test
    fun `addTaskToProject should handle when taskDescription is blank`() {
        // Arrange
        val projectId = UUID.randomUUID()

        // Act & Assert
        assertThrows(IllegalArgumentException::class.java) {
            projectService.addTaskToProject(projectId, "")
        }
    }

    @Test
    fun `addTaskToProject should handle when taskDescription is null`() {
        // Arrange
        val projectId = UUID.randomUUID()

        // Act & Assert
        assertThrows(IllegalArgumentException::class.java) {
            projectService.addTaskToProject(projectId, null)
        }
    }

    @Test
    fun `addTaskToProject should handle when projectId is null`() {
        // Arrange
        val projectId = null

        // Act & Assert
        assertThrows(IllegalArgumentException::class.java) {
            projectService.addTaskToProject(projectId, "")
        }
    }

    @Test
    fun `addTaskToProject should handle when projectId is not valid`() {
        // Act & Assert
        assertThrows(IllegalArgumentException::class.java) {
            projectService.addTaskToProject(UUID.fromString("some wrong Id"), "")
        }
    }
}