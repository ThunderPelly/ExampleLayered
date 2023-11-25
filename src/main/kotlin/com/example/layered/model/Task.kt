package com.example.layered.model

import java.util.*

data class Task(val taskId: UUID = UUID.randomUUID(), val description: String) {
    var priority: Int = 0
    var assignedUser: User? = null
    var isCompleted = false
}