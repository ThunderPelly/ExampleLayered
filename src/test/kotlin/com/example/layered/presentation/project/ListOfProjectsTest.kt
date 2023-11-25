package com.example.layered.presentation.project

import com.example.layered.application.ProjectService
import com.example.layered.model.Project
import com.example.layered.presentation.ProjectController
import com.example.layered.presentation.dto.ProjectResponseDto
import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.util.*

class ListOfProjectsTest {
    private val projectService: ProjectService = mockk()
    private val mockMvc: MockMvc = MockMvcBuilders.standaloneSetup(ProjectController(projectService))
        .build()

    @Test
    fun `listProjects should return a list of ProjectResponseDto`() {
        // Arrange
        val project1 = Project(UUID.randomUUID(), "Project1")
        val project2 = Project(UUID.randomUUID(), "Project2")
        every { projectService.getProjects() } returns mutableMapOf(
            project1.projectId to project1,
            project2.projectId to project2
        )

        val expectedResponse = listOf(
            ProjectResponseDto(project1.projectId, project1.name, project1.tasks),
            ProjectResponseDto(project2.projectId, project2.name, project2.tasks)
        )

        // Act & Assert
        performGetListOfProjectsRequest()
            .andExpect(status().isOk)
            .andExpect(content().json(ObjectMapper().writeValueAsString(expectedResponse)))
    }

    @Test
    fun `listProjects should handle empty list response`() {
        // Arrange
        // Simulating an empty list of projects
        every { projectService.getProjects() } returns mutableMapOf()

        val expectedResponse = listOf<ProjectResponseDto>() // Expecting an empty list

        // Act & Assert
        performGetListOfProjectsRequest()
            .andExpect(status().isOk)
            .andExpect(content().json(ObjectMapper().writeValueAsString(expectedResponse)))
    }

    private fun performGetListOfProjectsRequest(): ResultActions {
        return mockMvc.perform(
            get("/api/v1/project")
                .contentType(MediaType.APPLICATION_JSON)
        )
    }
}