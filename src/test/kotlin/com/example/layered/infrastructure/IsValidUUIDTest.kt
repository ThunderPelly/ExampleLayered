package com.example.layered.infrastructure

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

class IsValidUUIDTest {

    @Test
    fun `isValidUuid should return true for a valid UUID`() {
        // Arrange
        val validUuid = UUID.randomUUID()

        // Act
        val result = validUuid.isValidUuid()

        // Assert
        assertTrue(result)
    }

    @Test
    fun `isValidUuid should return false for a null UUID`() {
        // Arrange
        val nullUuid: UUID? = null

        // Act
        val result = nullUuid.isValidUuid()

        // Assert
        assertFalse(result)
    }

    @Test
    fun `isValidUuid should return false for an invalid UUID (blank string)`() {
        // Act & Assert
        assertThrows(IllegalArgumentException::class.java) {
            UUID.fromString("").isValidUuid()
        }
    }

    @Test
    fun `isValidUuid should return false for an invalid UUID (random string)`() {
        // Act & Assert
        assertThrows(IllegalArgumentException::class.java) {
            UUID.fromString("invalid-uuid").isValidUuid()
        }
    }
}