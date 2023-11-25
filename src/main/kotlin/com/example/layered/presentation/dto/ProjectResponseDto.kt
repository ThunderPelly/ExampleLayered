package com.example.layered.presentation.dto

import com.example.layered.model.Task
import java.util.*

data class ProjectResponseDto(val projectId: UUID?, val name: String?, val tasks: MutableList<Task>?) {
    constructor() : this(null, null, mutableListOf())
}