package com.example.layered.application.user

import com.example.layered.application.UserService
import com.example.layered.application.UserTaskService
import com.example.layered.model.Task
import com.example.layered.model.User
import com.example.layered.model.UserName
import com.example.layered.model.UserRole
import com.example.layered.persistence.TaskRepository
import com.example.layered.persistence.UserRepository
import io.mockk.MockKAnnotations
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetUserTasksTest {

    @MockK
    private lateinit var userRepository: UserRepository

    @MockK
    private lateinit var taskRepository: TaskRepository

    @InjectMockKs
    private lateinit var userService: UserService

    @InjectMockKs
    private lateinit var userTaskService: UserTaskService

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        clearMocks(userRepository)
        clearMocks(taskRepository)
    }

    @Test
    fun `getUserTasks should return null when user does not exist`() {
        // Arrange
        val userName = "nonexistentUser"
        every { userRepository.getUserByUsername(userName) } returns null

        // Act
        val result = userService.getUserTasks(userName)

        // Assert
        assertNull(result)
        verify(exactly = 1) { userRepository.getUserByUsername(userName) }
    }

    @Test
    fun `getUserTasks should return empty list when user has no tasks`() {
        // Arrange
        val userName = "userWithoutTasks"
        val user = User(userName = UserName(userName), role = UserRole.TEAM_MEMBER)
        every { userRepository.getUserByUsername(userName) } returns user

        // Act
        val result = userService.getUserTasks(userName)

        // Assert
        assertNotNull(result)
        assertTrue(result!!.isEmpty())
        verify(exactly = 1) { userRepository.getUserByUsername(userName) }
    }

    @Test
    fun `getUserTasks should return a list of tasks when user has tasks`() {
        // Arrange
        val userName = "userWithTasks"
        val taskList = listOf(Task(description = "Task1"), Task(description = "Task2"))
        val user = User(userName = UserName(userName), role = UserRole.TEAM_MEMBER)

        every { userRepository.getUserByUsername(userName) } returns user
        every { taskRepository.allTasks } returns taskList
        every { taskRepository.saveTask(taskList.get(0)) } returns taskList.get(0)
        every { taskRepository.saveTask(taskList.get(1)) } returns taskList.get(1)

        assignTaskToUser(userName, taskList.get(0).description)
        assignTaskToUser(userName, taskList.get(1).description)

        // Act
        val result = userService.getUserTasks(userName)

        // Assert
        assertNotNull(result)
        assertEquals(taskList.size, result!!.size)
        assertEquals(taskList, result)
    }


    @Test
    fun `getUserTasks should throw IllegalArgumentException when userName is null`() {
        // Act & Assert
        assertThrows(IllegalArgumentException::class.java) {
            userService.getUserTasks(null)
        }
        verify(exactly = 0) { userRepository.getUserByUsername(any()) }
    }

    @Test
    fun `getUserTasks should throw IllegalArgumentException when userName is empty`() {
        // Act & Assert
        assertThrows(IllegalArgumentException::class.java) {
            userService.getUserTasks("")
        }
        verify(exactly = 0) { userRepository.getUserByUsername(any()) }
    }

    private fun assignTaskToUser(username: String, taskDescription: String): Task? {
        return userTaskService.assignTaskToUser(username, taskDescription)
    }
}