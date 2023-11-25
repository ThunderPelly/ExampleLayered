package com.example.layered.presentation

import com.example.layered.application.UserTaskService
import com.example.layered.presentation.dto.TaskRequestDto
import com.example.layered.presentation.dto.TaskResponseDto
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/v1/user-task")
class UserTaskController(private val userTaskService: UserTaskService) {

    @PostMapping
    fun assignTaskToUser(
        @RequestParam userName: String,
        @RequestBody taskRequestDto: TaskRequestDto
    ): TaskResponseDto? {
        val task = userTaskService.assignTaskToUser(userName, taskRequestDto.description)
        return task?.let { TaskResponseDto(it.description) }
    }
}