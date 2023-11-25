package com.example.layered.presentation.dto

import com.example.layered.model.UserRole

data class UserRequestDto(val userName: String?, val role: UserRole?) {
    constructor() : this(null, null)
}