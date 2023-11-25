package com.example.layered.infrastructure

import java.util.*

fun UUID?.isValidUuid(): Boolean {
    return this != null && this.toString().isNotBlank()
}
