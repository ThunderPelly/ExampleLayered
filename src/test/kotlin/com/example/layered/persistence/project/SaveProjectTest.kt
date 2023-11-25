package com.example.layered.persistence.project

import com.example.layered.model.Project
import com.example.layered.persistence.ProjectRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*

class SaveProjectTest {

    @Test
    fun `saveProject should add a project to the repository`() {
        // Arrange
        val projectRepository = ProjectRepository()
        val projectId = UUID.randomUUID()
        val project = Project(projectId, "Project 1")

        // Act
        val savedProject = projectRepository.saveProject(project)

        // Assert
        assertEquals(project, savedProject)
        assertEquals(project, projectRepository.getProjectById(projectId))
    }
}