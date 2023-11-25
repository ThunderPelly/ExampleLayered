package com.example.layered.persistence.project

import com.example.layered.model.Project
import com.example.layered.persistence.ProjectRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*

class AllProjectsTest {
    @Test
    fun `allProjects should return all projects in the repository`() {
        // Arrange
        val projectRepository = ProjectRepository()
        val project1 = Project(UUID.randomUUID(), "Project 1")
        val project2 = Project(UUID.randomUUID(), "Project 2")
        projectRepository.saveProject(project1)
        projectRepository.saveProject(project2)

        // Act
        val result = projectRepository.allProjects

        // Assert
        assertEquals(2, result.size)
        assertEquals(project1, result[project1.projectId])
        assertEquals(project2, result[project2.projectId])
    }
}