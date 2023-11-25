package com.example.layered.presentation.dto

data class TaskResponseDto(val description: String?) {
    constructor() : this(null)
}