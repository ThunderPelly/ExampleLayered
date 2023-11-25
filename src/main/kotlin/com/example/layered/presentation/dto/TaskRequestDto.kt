package com.example.layered.presentation.dto

data class TaskRequestDto(val description: String?) {
    constructor() : this(null)
}