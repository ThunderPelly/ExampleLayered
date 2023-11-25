package com.example.layered.persistence

import com.example.layered.model.Task
import org.springframework.stereotype.Repository

@Repository
class TaskRepository {
    private var tasks: MutableList<Task> = ArrayList()

    fun saveTask(task: Task): Task {
        tasks.add(task)
        return task
    }

    val allTasks: List<Task>
        get() = tasks
}