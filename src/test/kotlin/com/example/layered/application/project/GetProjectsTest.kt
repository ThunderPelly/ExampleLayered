package com.example.layered.application.project

import com.example.layered.application.ProjectService
import com.example.layered.model.Project
import com.example.layered.persistence.ProjectRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*

class GetProjectsTest {


    private val projectRepository: ProjectRepository = mockk(relaxed = true)
    private val projectService = ProjectService(projectRepository)

    @Test
    fun `getProjects should return all projects from the repository`() {
        // Arrange
        val project1 = Project(UUID.randomUUID(), "Project 1")
        val project2 = Project(UUID.randomUUID(), "Project 2")
        every { projectRepository.allProjects } returns mutableMapOf(
            project1.projectId to project1,
            project2.projectId to project2
        )

        // Act
        val result = projectService.getProjects()

        // Assert
        assertEquals(2, result.size)
        assertEquals(project1, result[project1.projectId])
        assertEquals(project2, result[project2.projectId])
    }

    @Test
    fun `getProjects should return an empty map when no projects are in the repository`() {
        // Arrange
        every { projectRepository.allProjects } returns mutableMapOf()

        // Act
        val result = projectService.getProjects()

        // Assert
        assertEquals(0, result.size)
    }
}