package com.example.layered.presentation

import com.example.layered.application.ProjectService
import com.example.layered.presentation.dto.ProjectRequestDto
import com.example.layered.presentation.dto.ProjectResponseDto
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("/api/v1/project")
class ProjectController(private val projectService: ProjectService) {

    @PostMapping
    fun createProject(@RequestBody projectRequestDto: ProjectRequestDto): ProjectResponseDto? {
        val project = projectService.createProject(projectRequestDto.name, projectRequestDto.role)
        return project?.let { ProjectResponseDto(it.projectId, project.name, it.tasks) }
    }

    @GetMapping
    fun listProjects(): List<ProjectResponseDto> {
        val projects = projectService.getProjects()
        return projects.map { (projectId, project) -> ProjectResponseDto(projectId, project.name, project.tasks) }
    }

    @PutMapping("/{projectId}/add-task")
    fun addTaskToProject(
        @PathVariable projectId: UUID,
        @RequestParam taskDescription: String
    ): ProjectResponseDto? {
        val project = projectService.addTaskToProject(projectId, taskDescription)
        return ProjectResponseDto(project?.projectId, project?.projectName?.value, project?.tasks)
    }
}