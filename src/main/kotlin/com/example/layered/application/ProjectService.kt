package com.example.layered.application

import com.example.hexagonal.domain.application.exceptions.InsufficientPermissionException
import com.example.layered.infrastructure.isValidUuid
import com.example.layered.model.Project
import com.example.layered.model.Task
import com.example.layered.model.UserRole
import com.example.layered.persistence.ProjectRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class ProjectService(private val projectRepository: ProjectRepository) {
    private val projectExceptionMessage = "Die Projekt-Id darf nicht null oder leer sein."
    private val descriptionExceptionMessage = "Die Aufgabenbeschreibung  darf nicht null oder leer sein."
    private val projectNameExceptionMessage = "Der Projektname darf nicht null oder leer sein."
    private val insufficientPermissionExceptionMessage =
        "Benutzer hat nicht die erforderliche Rolle, um ein Projekt zu erstellen."

    /**
     * Erstellt ein neues Projekt mit dem angegebenen Projektname und speichert es im Projekt-Repository.
     *
     * @param projectName Der Name des neuen Projekts.
     * @return Das erstellte Projekt als Project-Objekt.
     * @throws IllegalArgumentException Wenn der Projektname ungültig ist.
     * @throws InsufficientPermissionException Wenn der Benutzer nicht die erforderliche Rolle hat, um ein Projekt zu erstellen.
     *
     */
    fun createProject(projectName: String?, role: UserRole?): Project? {
        // Überprüfen, ob der Benutzer die erforderliche Rolle hat, um ein Projekt zu erstellen
        if (role == UserRole.MANAGER) {
            // Überprüfen, ob der Projektname nicht null oder leer ist
            require(!projectName.isNullOrBlank()) { projectNameExceptionMessage }
            val project = Project(name = projectName)
            projectRepository.saveProject(project)
            return project
        } else {
            throw InsufficientPermissionException(insufficientPermissionExceptionMessage)
        }
    }

    /**
     * Ruft alle Projekte aus dem Projekt-Repository ab und gibt sie als MutableMap von UUIDs zu Project-Objekten zurück.
     *
     * @return Eine MutableMap, die die abgerufenen Projekte repräsentiert.
     */
    fun getProjects(): MutableMap<UUID, Project> {
        return projectRepository.allProjects
    }

    /**
     * Fügt eine Aufgabe zu einem Projekt hinzu und gibt das aktualisierte Projekt als ProjectResponseDto-Objekt zurück.
     *
     * @param projectId Die UUID des Projekts, zu dem die Aufgabe hinzugefügt werden soll.
     * @param taskDescription Die Beschreibung der hinzuzufügenden Aufgabe.
     * @return Ein ProjectResponseDto-Objekt, das das aktualisierte Projekt repräsentiert, oder null, wenn das Projekt nicht gefunden wird.
     * @throws IllegalArgumentException Wenn die Projekt-Id oder die Aufgabenbeschreibung ungültig ist.
     */
    fun addTaskToProject(projectId: UUID?, taskDescription: String?): Project? {
        // Überprüfen, ob der Projekt-Id nicht null oder leer ist
        require(projectId.isValidUuid()) { projectExceptionMessage }
        require(!taskDescription.isNullOrBlank()) { descriptionExceptionMessage }
        val project = projectId?.let { projectRepository.getProjectById(it) }
        val task = Task(description = taskDescription)
        project?.let {
            it.tasks.add(task)
            projectRepository.saveProject(project)
        }
        return project
    }
}