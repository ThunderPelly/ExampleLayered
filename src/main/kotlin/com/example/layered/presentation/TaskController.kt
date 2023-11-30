package com.example.layered.presentation

import com.example.layered.application.TaskService
import com.example.layered.presentation.dto.TaskRequestDto
import com.example.layered.presentation.dto.TaskResponseDto
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/task")
class TaskController(private val taskService: TaskService) {

    @PostMapping
    fun createTask(@RequestBody taskRequestDto: TaskRequestDto): TaskResponseDto? {
        val task = taskService.createTask(taskRequestDto.description)
        return task?.description?.let { TaskResponseDto(it.value) }
    }

    @GetMapping
    fun listTasks(): List<TaskResponseDto>? {
        val tasks = taskService.getAllTasks()
        return tasks?.map { TaskResponseDto(it.description.value) }
    }

    @PutMapping("/{taskDescription}/priority")
    fun setTaskPriority(
        @PathVariable taskDescription: String,
        @RequestParam priority: Int
    ): TaskResponseDto? {
        val task = taskService.setTaskPriority(taskDescription, priority)
        return task?.description?.let { TaskResponseDto(it.value) }
    }

    @PutMapping("/{taskDescription}/complete")
    fun completeTask(
        @PathVariable taskDescription: String,
    ): TaskResponseDto? {
        val task = taskService.completeTask(taskDescription)
        return task?.description?.let { TaskResponseDto(it.value) }
    }
}