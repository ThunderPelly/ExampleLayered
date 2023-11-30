package com.example.layered.application

import com.example.layered.model.Task
import com.example.layered.model.TaskPriority
import com.example.layered.model.User
import com.example.layered.persistence.TaskRepository
import com.example.layered.persistence.UserRepository
import org.springframework.stereotype.Service

@Service
class UserTaskService(
    private val userRepository: UserRepository,
    private val taskRepository: TaskRepository
) {
    private val businessLogicException = "To many low prio Tasks"
    private val userNameExceptionMessage = "Der Benutzername darf nicht null oder leer sein."
    private val userOrTaskExceptionMessage = "Benutzer und Aufgabe müssen existieren."
    private val completedTaskExceptionMessage = "Kann keine abgeschlossene Aufgabe einem Benutzer zuweisen."

    /**
     * Weist eine Aufgabe einem Benutzer zu, basierend auf bestimmten Bedingungen, und gibt die zugewiesene Aufgabe zurück.
     *
     * @param userName Der Benutzername, dem die Aufgabe zugewiesen werden soll.
     * @param taskDescription Die Beschreibung der Aufgabe, die zugewiesen werden soll.
     * @return Das Task-Objekt, das die zugewiesene Aufgabe repräsentiert, oder null, wenn die Zuweisung fehlschlägt.
     * @throws IllegalArgumentException Wenn Benutzer oder Aufgabe nicht existieren.
     */
    fun assignTaskToUser(userName: String?, taskDescription: String?): Task? {
        val user = userRepository.getUserByUsername(userName)
        val task = taskRepository.allTasks.find { it.description.value == taskDescription }
        requireNotNull(userName) { userNameExceptionMessage }

        require(!(user == null || task == null)) { userOrTaskExceptionMessage }

        // Simulierte Geschäftslogik: Je nach bestimmten Bedingungen Aufgaben zuweisen oder neu zuweisen
        when {
            shouldAssignTask(user) -> assignTask(user, task)
            shouldReassignTask(user) -> reassignTask(user, task)
            else -> {
                throw IllegalStateException(businessLogicException)
            }
        }

        return task
    }

    /**
     * Determines whether a task should be assigned to the user based on a business rule.
     *
     * @param user The user to check for task assignment eligibility.
     * @return True if a task should be assigned, false otherwise.
     */
    private fun shouldAssignTask(user: User): Boolean {
        return user.assignedTasks.size < 3
    }

    /**
     * Determines whether a task should be reassigned to the user based on a business rule.
     *
     * @param user The user to check for task reassignment eligibility.
     * @return True if a task should be reassigned, false otherwise.
     */
    private fun shouldReassignTask(user: User): Boolean {
        return user.assignedTasks.any { it.priority.value > 3 }
    }

    /**
     * Weist eine Aufgabe einem Benutzer zu und speichert die Änderungen im Task-Repository.
     *
     * @param user Der Benutzer, dem die Aufgabe zugewiesen wird.
     * @param task Die Aufgabe, die zugewiesen wird.
     * @throws IllegalArgumentException Wenn versucht wird, eine abgeschlossene Aufgabe einem Benutzer zuzuweisen.
     */
    private fun assignTask(user: User, task: Task) {
        check(!task.isCompleted) { completedTaskExceptionMessage }

        task.assignedUser = user
        user.assignedTasks.add(task)
        taskRepository.saveTask(task)
    }

    /**
     * Weist eine Aufgabe einem Benutzer zu. Der neue Task erhält die höchste Priorität aller Tasks.
     *
     * @param user Der Benutzer, dem die Aufgabe neu zugewiesen wird.
     * @param task Die Aufgabe, die neu zugewiesen wird.
     */
    private fun reassignTask(user: User, task: Task) {
        val highestPriority = user.assignedTasks.maxByOrNull { it.priority.value }!!.priority
        task.priority = TaskPriority(highestPriority.value + 1)
        task.assignedUser = user
        user.assignedTasks.add(task)

        taskRepository.saveTask(task)
    }
}