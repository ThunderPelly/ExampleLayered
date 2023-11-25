package com.example.layered.application

import com.example.layered.model.Task
import com.example.layered.model.User
import com.example.layered.model.UserName
import com.example.layered.model.UserRole
import com.example.layered.persistence.UserRepository
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.DefaultTransactionDefinition

@Service
class UserService(private val userRepository: UserRepository) {
    private val userNameExceptionMessage = "Der Benutzername darf nicht null oder leer sein."

    private final val defaultUserRole: UserRole = UserRole.TEAM_MEMBER

    @Autowired
    private lateinit var transactionManager: PlatformTransactionManager

    private val userCreationLock = Any() // Ein Sperrobjekt


    /**
     * Erstellt einen neuen Benutzer mit dem angegebenen Benutzernamen, Rolle und speichert ihn im Benutzer-Repository.
     *
     * @param userName Der Benutzername des neuen Benutzers.
     * @param role Die Rolle des neuen Benutzers. Standardwert (Teammitglied) wenn leer
     * @return Das User-Objekt, das den erstellten Benutzer repräsentiert, oder null, wenn die Erstellung fehlschlägt.
     * @throws IllegalArgumentException Wenn der Benutzername ungültig ist.
     */
    @Transactional
    fun createUser(userName: String?, role: UserRole?): User? {
        // Setze die Standardrolle, falls die bereitgestellte Rolle null oder leer ist
        val userRole = role ?: defaultUserRole

        // Sperren, um sicherzustellen, dass nur ein Thread createUser gleichzeitig ausführt
        synchronized(userCreationLock) {
            // Erstellen einer Transaktion
            val transactionDefinition = DefaultTransactionDefinition()
            val transactionStatus = transactionManager.getTransaction(transactionDefinition)

            try {
                // Erstellen eines eindeutigen Benutzernamens
                val uniqueUserName = createUniqueUserName(userName)

                // Benutzer erstellen und im Repository speichern
                val user = User(userName = uniqueUserName, role = userRole)

                userRepository.saveUser(user)

                // Transaktion erfolgreich abschließen
                transactionManager.commit(transactionStatus)

                return user
            } catch (e: Exception) {
                // Transaktion rückgängig machen und Ausnahme weitergeben
                transactionManager.rollback(transactionStatus)
                throw e
            }
        }
    }

    /**
     * Erstellt einen eindeutigen Benutzernamen basierend auf dem gegebenen Benutzernamen.
     * Wenn der Benutzername bereits existiert, wird ein Suffix hinzugefügt, um die Eindeutigkeit sicherzustellen.
     *
     * @param userName Der ursprüngliche Benutzername.
     * @return Ein eindeutiger Benutzername.
     */
    fun createUniqueUserName(userName: String?): UserName {
        var newUserName = UserName(userName)
        var suffix = 0

        // Überprüfe, ob der Benutzername bereits existiert, und füge ein Suffix hinzu, um die Eindeutigkeit sicherzustellen
        while (userRepository.getUserByUsername(newUserName.value) != null) {
            suffix++
            newUserName = UserName(newUserName.value, suffix)
        }

        return newUserName
    }

    /**
     * Ruft alle Benutzer aus dem Repository ab und gibt sie als Liste von User-Objekten zurück.
     *
     * @return Eine Liste von User-Objekten, die die abgerufenen Benutzer repräsentieren.
     */
    fun getAllUsers(): List<User>? {
        return userRepository.allUsers
    }

    /**
     * Ruft die Aufgaben eines bestimmten Benutzers aus dem Repository ab und gibt sie als Liste von Task-Objekten zurück.
     *
     * @param userName Der Benutzername, dessen Aufgaben abgerufen werden sollen.
     * @return Eine Liste von Task-Objekten, die die abgerufenen Aufgaben des Benutzers repräsentieren, oder null, wenn der Benutzer nicht gefunden wird.
     * @throws IllegalArgumentException Wenn der Benutzername ungültig ist.
     */
    fun getUserTasks(userName: String?): List<Task>? {
        // Überprüfen, ob der Benutzername nicht null oder leer ist
        require(!userName.isNullOrBlank()) { userNameExceptionMessage }

        val user = userRepository.getUserByUsername(userName)
        return user?.assignedTasks
    }

}