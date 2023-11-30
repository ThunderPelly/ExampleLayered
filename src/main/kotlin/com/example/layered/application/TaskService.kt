package com.example.layered.application

import com.example.layered.model.Task
import com.example.layered.model.TaskDescription
import com.example.layered.model.TaskPriority
import com.example.layered.persistence.TaskRepository
import org.springframework.stereotype.Service

@Service
class TaskService(private val taskRepository: TaskRepository) {
    private val descriptionExceptionMessage = "Die Beschreibung darf nicht null oder leer sein."
    private val priorityExceptionMessage = "Die Priorität darf nicht null oder leer sein."

    /**
     * Erstellt eine neue Aufgabe mit der angegebenen Beschreibung (sofern nicht null oder leer) und speichert sie im Task-Repository.
     *
     * @param description Die Beschreibung der neuen Aufgabe.
     * @throws IllegalArgumentException Wenn die Task-Beschreibung ungültig ist.
     */
    fun createTask(description: String?): Task? {
        val task = Task(description = TaskDescription(description))
        taskRepository.saveTask(task)
        return task
    }

    /**
     * Ruft alle Aufgaben aus dem Task-Repository ab und gibt sie als Liste von Task-Objekten zurück.
     *
     * @return Eine Liste von Task-Objekten, die die abgerufenen Aufgaben repräsentieren.
     */
    fun getAllTasks(): List<Task>? {
        return taskRepository.allTasks
    }

    /**
     * Markiert eine Aufgabe mit der angegebenen Beschreibung als abgeschlossen und speichert die Änderung im Task-Repository.
     *
     * @param description Die Beschreibung der abzuschließenden Aufgabe.
     * @return Die aktualisierte Task-Objekt, das die abgeschlossene Aufgabe repräsentiert, oder null, wenn die Aufgabe nicht gefunden wird.
     * @throws IllegalArgumentException Wenn die Task-Beschreibung ungültig ist.
     */
    fun completeTask(description: String?): Task? {   // Überprüfen, ob der Beschreibung und die Priorität nicht null oder leer ist
        require(!description.isNullOrBlank()) { descriptionExceptionMessage }

        val task = taskRepository.allTasks.find { it.description.value == description }
        task?.let {
            it.isCompleted = true
            taskRepository.saveTask(it)
        }
        return task
    }

    /**
     * Setzt die Priorität einer Aufgabe mit der angegebenen Beschreibung und speichert die Änderung im Task-Repository.
     *
     * @param description Die Beschreibung der Aufgabe, deren Priorität geändert werden soll.
     * @param priority Die neue Priorität für die Aufgabe.
     * @return Das Task-Objekt, das die aktualisierte Aufgabe repräsentiert, oder null, wenn die Aufgabe nicht gefunden wird.
     * @throws IllegalArgumentException Wenn die Task-Beschreibung oder die Priorität ungültig ist.
     */
    fun setTaskPriority(description: String?, priority: Int?): Task? {
        // Überprüfen, ob der Beschreibung und die Priorität nicht null oder leer ist
        require(priority != null && priority > 0) { priorityExceptionMessage }
        val task = taskRepository.allTasks.find { it.description.value == description }
        task?.let {
            it.priority = TaskPriority(priority)
            taskRepository.saveTask(it)
        }
        return task
    }
}