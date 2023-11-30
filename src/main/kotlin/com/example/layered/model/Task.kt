package com.example.layered.model

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import java.util.*

@JvmInline
value class TaskDescription(val value: String) {
    init {
        // eine IllegalArgumentException auslösen, wenn taskDescription leer ist
        require(value.isNotBlank()) { "Die Aufgabenbeschreibung darf nicht null oder leer sein." }
    }

    companion object {
        operator fun invoke(value: String?): TaskDescription {
            // eine IllegalArgumentException auslösen, wenn taskDescription leer ist
            requireNotNull(value) { "Die Aufgabenbeschreibung darf nicht null sein." }

            return TaskDescription(value)
        }
    }
}

@JvmInline
value class TaskPriority(val value: Int = 0) {
    init {
        // eine IllegalArgumentException auslösen, wenn taskPriority kleiner 1 ist
        require(value >= 0) { "Die Priorität darf nicht negativ sein." }
    }

    companion object {
        operator fun invoke(value: Int?): TaskPriority {
            // eine IllegalArgumentException auslösen, wenn taskPriority null ist
            requireNotNull(value) { "Die Priorität darf nicht null sein." }
            // eine IllegalArgumentException auslösen, wenn taskPriority kleiner 1 ist
            require(value >= 0) { "Die Priorität darf nicht negativ sein." }

            return TaskPriority(value)
        }
    }
}

@JsonSerialize(using = TaskSerializer::class)
class Task(
    val taskId: UUID = UUID.randomUUID(),
    val description: TaskDescription,
    var priority: TaskPriority = TaskPriority(0),
    var isCompleted: Boolean = false,
    var assignedUser: User? = null
)

@JsonSerialize(using = TaskSerializer::class)
class TaskSerializer : JsonSerializer<Task>() {
    override fun serialize(value: Task?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        gen?.writeStartObject()
        gen?.writeStringField("taskId", value?.taskId.toString())
        gen?.writeStringField("description", value?.description?.value)
        value?.isCompleted?.let { gen?.writeBooleanField("isCompleted", it) }
        value?.assignedUser?.let { user ->
            gen?.writeFieldName("assignedUser")
            serializers?.defaultSerializeValue(user, gen)
        }
        gen?.writeEndObject()
    }
}