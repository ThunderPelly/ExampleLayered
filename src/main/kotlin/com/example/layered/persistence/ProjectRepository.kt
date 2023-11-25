package com.example.layered.persistence

import com.example.layered.model.Project
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class ProjectRepository {
    private val projects: MutableMap<UUID, Project> = mutableMapOf()

    fun saveProject(project: Project): Project {
        projects[project.projectId] = project
        return project
    }

    fun getProjectById(projectId: UUID): Project? {
        return projects[projectId]
    }

    val allProjects: MutableMap<UUID, Project>
        get() = projects
}