package com.example.layered.presentation.project

import com.example.layered.application.ProjectService
import com.example.layered.model.Project
import com.example.layered.model.ProjectName
import com.example.layered.model.UserRole
import com.example.layered.presentation.ProjectController
import com.example.layered.presentation.dto.ProjectRequestDto
import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.util.*

class CreateProjectTest {
    private val projectService: ProjectService = mockk()
    private val mockMvc: MockMvc = MockMvcBuilders.standaloneSetup(ProjectController(projectService)).build()

    @Test
    fun `createProject should return created project`() {
        // Arrange
        val projectId = UUID.randomUUID()
        val projectName = "Test Project"
        val project = Project(projectId, ProjectName(projectName))
        every { projectService.createProject(projectName, UserRole.MANAGER) } returns project

        val requestDto = ProjectRequestDto(name = projectName, role = UserRole.MANAGER)
        val requestBody = ObjectMapper().writeValueAsString(requestDto)

        // Act & Assert
        performCreateProjectRequest(requestBody).andExpect(status().isOk)
    }


    @Test
    fun `createProject should handle null project response`() {
        // Arrange
        val projectName = "Test Project"

        // Simulating a scenario where project creation fails
        every { projectService.createProject(projectName, UserRole.MANAGER) } returns null

        val requestDto = ProjectRequestDto(name = projectName, role = UserRole.MANAGER)
        val requestBody = ObjectMapper().writeValueAsString(requestDto)

        // Act & Assert
        performCreateProjectRequest(requestBody)
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.projectId").doesNotExist())
            .andExpect(jsonPath("$.name").doesNotExist())
    }

    private fun performCreateProjectRequest(requestBody: String): ResultActions {
        return mockMvc.perform(
            post("/api/v1/project")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
        )
    }
}