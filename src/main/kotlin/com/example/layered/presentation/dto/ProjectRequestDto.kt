package com.example.layered.presentation.dto

import com.example.layered.model.UserRole

data class ProjectRequestDto(val name: String?, val role: UserRole?) {
    constructor() : this(null, null)
}

