package com.example.layered.application.project

import com.example.hexagonal.domain.application.exceptions.InsufficientPermissionException
import com.example.layered.application.ProjectService
import com.example.layered.model.UserRole
import com.example.layered.persistence.ProjectRepository
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class CreateProjectTest {

    private val projectRepository: ProjectRepository = mockk(relaxed = true)
    private val projectService = ProjectService(projectRepository)

    @Test
    fun `createProject should create and save project when user has manager role`() {
        // Arrange
        val projectName = "Test Project"
        val managerRole = UserRole.MANAGER

        // Act
        val result = projectService.createProject(projectName, managerRole)

        // Assert
        verify(exactly = 1) { projectRepository.saveProject(result!!) }
        assertEquals(projectName, result!!.name)
    }

    @Test
    fun `createProject should throw InsufficientPermissionException when user does not have manager role`() {
        // Arrange
        val projectName = "Test Project"
        val teamMemberRole = UserRole.TEAM_MEMBER

        // Act & Assert
        assertThrows(InsufficientPermissionException::class.java) {
            projectService.createProject(projectName, teamMemberRole)
        }
        verify(exactly = 0) { projectRepository.saveProject(any()) }
    }

    @Test
    fun `createProject should throw IllegalArgumentException when project name is blank`() {
        // Arrange
        val projectName = ""
        val managerRole = UserRole.MANAGER

        // Act & Assert
        assertThrows(IllegalArgumentException::class.java) {
            projectService.createProject(projectName, managerRole)
        }
        verify(exactly = 0) { projectRepository.saveProject(any()) }
    }

    @Test
    fun `createProject should throw IllegalArgumentException when project name is null`() {
        // Arrange
        val managerRole = UserRole.MANAGER

        // Act & Assert
        assertThrows(IllegalArgumentException::class.java) {
            projectService.createProject(null, managerRole)
        }
        verify(exactly = 0) { projectRepository.saveProject(any()) }
    }
}