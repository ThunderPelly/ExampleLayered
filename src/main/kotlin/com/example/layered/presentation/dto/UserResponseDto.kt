package com.example.layered.presentation.dto

import com.example.layered.model.UserRole

data class UserResponseDto(val userName: String?, val role: UserRole?) {
    constructor() : this(null, null)
}