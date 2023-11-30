package com.example.layered.application.userTask

import com.example.layered.application.UserTaskService
import com.example.layered.model.*
import com.example.layered.persistence.TaskRepository
import com.example.layered.persistence.UserRepository
import io.mockk.MockKAnnotations
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AssignTaskToUserTest {

    @MockK
    private var userRepository: UserRepository = mockk<UserRepository>()

    @MockK
    private var taskRepository: TaskRepository = mockk<TaskRepository>()

    @InjectMockKs
    private var userTaskService: UserTaskService = UserTaskService(userRepository, taskRepository)

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        clearMocks(userRepository)
        clearMocks(taskRepository)
    }

    @Test
    fun `assignTaskToUser should assign a task to a user successfully`() {
        // Arrange
        val userName = "john.doe"
        val user = User(userName = UserName(userName), role = UserRole.TEAM_MEMBER)
        val taskDescription = "New Task"
        val task = Task(description = TaskDescription(taskDescription))
        val allTasks = listOf(task)
        every { userRepository.getUserByUsername(userName) } returns user
        every { taskRepository.allTasks } returns allTasks
        every { taskRepository.saveTask(any()) } returns Task(description = TaskDescription(taskDescription))

        // Act
        val result = userTaskService.assignTaskToUser(userName, taskDescription)

        // Assert
        assertNotNull(result)
        assertEquals(allTasks[0], result)
        assertEquals(1, user.assignedTasks.size)
    }

    @Test
    fun `assignTaskToUser should not assign a task when user does not exist`() {
        // Arrange
        val userName = "john.doe"
        val taskDescription = "New Task"

        every { userRepository.getUserByUsername(userName) } returns null
        every { taskRepository.allTasks } returns listOf(Task(description = TaskDescription(taskDescription)))

        // Act & Assert
        assertThrows(IllegalArgumentException::class.java) {
            userTaskService.assignTaskToUser(userName, taskDescription)
        }
    }

    @Test
    fun `assignTaskToUser should not assign a task when task does not exist`() {
        // Arrange
        val userName = "john.doe"
        val taskDescription = "New Task"

        every { userRepository.getUserByUsername(userName) } returns User(
            userName = UserName(userName),
            role = UserRole.TEAM_MEMBER
        )
        every { taskRepository.allTasks } returns emptyList()

        // Act & Assert
        assertThrows(IllegalArgumentException::class.java) {
            userTaskService.assignTaskToUser(userName, taskDescription)
        }
    }

    @Test
    fun `assignTaskToUser should not assign a task when task is completed`() {
        // Arrange
        val userName = "john.doe"
        val taskDescription = "New Task"
        val user = User(userName = UserName(userName), role = UserRole.TEAM_MEMBER)
        val task = Task(description = TaskDescription(taskDescription))
        task.isCompleted = true
        user.assignedTasks.add(task)

        every { userRepository.getUserByUsername(userName) } returns user
        every { taskRepository.allTasks } returns listOf(task)
        every { taskRepository.saveTask(any()) } returns Task(description = TaskDescription(taskDescription))

        // Act & Assert
        assertThrows(IllegalStateException::class.java) {
            userTaskService.assignTaskToUser(userName, taskDescription)
        }
    }

    @Test
    fun `assignTaskToUser should assign a task to a user with a high-priority assigned task`() {
        // Arrange
        val userName = "john.doe"
        val taskDescription = "New Task"
        val user = User(userName = UserName(userName), role = UserRole.TEAM_MEMBER)
        val highPrioTask = Task(description = TaskDescription("Highprio Task"))
        highPrioTask.priority = TaskPriority(4)
        val tasks = listOf(
            highPrioTask,
            Task(description = TaskDescription(taskDescription)),
            Task(description = TaskDescription("Task3")),
            Task(description = TaskDescription("Task4")),
            Task(description = TaskDescription("Task5"))
        )
        tasks.map {
            user.assignedTasks.add(it)
        }

        every { userRepository.getUserByUsername(userName) } returns user
        every { taskRepository.allTasks } returns tasks
        every { taskRepository.saveTask(any()) } returns Task(description = TaskDescription(taskDescription))

        // Act
        val result = userTaskService.assignTaskToUser(userName, taskDescription)

        // Assert
        assertEquals(tasks[1], result)
        assertEquals(6, user.assignedTasks.size)
        assertEquals(tasks[0].priority.value, 4)
        assertEquals(tasks[0], user.assignedTasks[0])
    }

    @Test
    fun `reassignTask should not reassign a task when there are to many low prio tasks`() {
        // Arrange
        val userName = "john.doe"
        val taskDescription = "New Task"
        val user = User(userName = UserName(userName), role = UserRole.TEAM_MEMBER)
        val tasks = listOf(
            Task(description = TaskDescription(taskDescription)),
            Task(description = TaskDescription("Task2")),
            Task(description = TaskDescription("Task3")),
            Task(description = TaskDescription("Task4"))
        )
        tasks.map {
            user.assignedTasks.add(it)
        }

        every { userRepository.getUserByUsername(userName) } returns user
        every { taskRepository.allTasks } returns tasks
        every { taskRepository.saveTask(any()) } returns Task(description = TaskDescription(taskDescription))

        // Act & Assert
        assertThrows(IllegalStateException::class.java) {
            userTaskService.assignTaskToUser(userName, taskDescription)
        }
    }
}