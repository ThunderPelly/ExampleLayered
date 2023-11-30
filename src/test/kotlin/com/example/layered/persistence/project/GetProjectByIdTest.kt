package com.example.layered.persistence.project

import com.example.layered.model.Project
import com.example.layered.model.ProjectName
import com.example.layered.persistence.ProjectRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*

class GetProjectByIdTest {
    @Test
    fun `getProjectById should handle non-existing project`() {
        // Arrange
        val projectRepository = ProjectRepository()
        val projectId = UUID.randomUUID()

        // Act
        val result = projectRepository.getProjectById(projectId)

        // Assert
        assertEquals(null, result)
    }

    @Test
    fun `getProjectById should return the project for an existing project`() {
        // Arrange
        val projectRepository = ProjectRepository()
        val projectId = UUID.randomUUID()
        val project = Project(projectId, ProjectName("Project 1"))
        projectRepository.saveProject(project)

        // Act
        val result = projectRepository.getProjectById(projectId)

        // Assert
        assertEquals(project, result)
    }

}