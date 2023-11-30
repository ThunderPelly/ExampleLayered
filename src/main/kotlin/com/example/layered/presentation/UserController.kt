package com.example.layered.presentation

import com.example.layered.application.UserService
import com.example.layered.presentation.dto.TaskResponseDto
import com.example.layered.presentation.dto.UserRequestDto
import com.example.layered.presentation.dto.UserResponseDto
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/user")
class UserController(private val userService: UserService) {

    @PostMapping
    fun createUser(@RequestBody userRequestDto: UserRequestDto): UserResponseDto? {
        val user = userService.createUser(userRequestDto.userName, userRequestDto.role)
        return user?.let { UserResponseDto(it.userName.value, it.role) }
    }

    @GetMapping
    fun listUsers(): List<UserResponseDto>? {
        val users = userService.getAllUsers()
        return users?.map { UserResponseDto(it.userName.value, it.role) }
    }

    @GetMapping("task")
    fun getUserTasks(@RequestParam username: String): List<TaskResponseDto>? {
        val tasks = userService.getUserTasks(username)
        return tasks?.map { TaskResponseDto(it.description.value) }
    }
}