package com.example.layered.model

import java.util.*

data class User(val userId: UUID = UUID.randomUUID(), val userName: String, val role: UserRole) {
    val assignedTasks: MutableList<Task> = ArrayList()
}
