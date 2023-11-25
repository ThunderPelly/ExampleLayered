package com.example.layered.model

import java.util.*
import kotlin.math.min

@JvmInline
value class UserName private constructor(val value: String) {
    init {
        // eine IllegalArgumentException ausl�sen, wenn userName leer ist
        require(value.isNotBlank()) { "Der Benutzername darf nicht leer sein." }
    }

    companion object {
        operator fun invoke(value: String?, suffix: Int = 0): UserName {
            // eine IllegalArgumentException ausl�sen, wenn userName leer ist
            requireNotNull(value) { "Der Benutzername darf nicht null sein." }
            // begrenzen der L�nge des Basisbenutzernamens auf maximal 5 Zeichen
            val truncatedValue = value.substring(0, min(value.length, 5))
            // anh�ngen des Suffixes an den abgeschnittenen Wert
            val finalValue = if (suffix > 0) "$truncatedValue$suffix" else truncatedValue

            return UserName(finalValue)
        }
    }
}

data class User(val userId: UUID = UUID.randomUUID(), val userName: UserName, val role: UserRole) {
    val assignedTasks: MutableList<Task> = ArrayList()
}