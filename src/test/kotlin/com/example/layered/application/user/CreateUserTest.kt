package com.example.layered.application.user

import com.example.layered.application.UserService
import com.example.layered.model.User
import com.example.layered.model.UserName
import com.example.layered.model.UserRole
import com.example.layered.persistence.UserRepository
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionStatus

class CreateUserTest {

    @MockK
    private var userRepository: UserRepository = mockk<UserRepository>()

    @MockK
    lateinit var transactionManager: PlatformTransactionManager

    @InjectMockKs
    private var userService: UserService = UserService(userRepository)

    private val defaultUserRole: UserRole = UserRole.TEAM_MEMBER

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        clearMocks(userRepository)
        clearMocks(transactionManager)
        every { transactionManager.rollback(any()) } just Runs
    }

    @Test
    fun `createUser should return user when successful`() {
        // Arrange
        val userName = "john.doe"
        val userRole = UserRole.MANAGER

        every { userRepository.saveUser(any()) } returns User(userName = UserName("john."), role = userRole)
        every { transactionManager.getTransaction(any()) } returns mockk<TransactionStatus>()
        every { transactionManager.commit(any()) } just Runs
        every { userRepository.getUserByUsername("john.") } returns null

        // Act
        val result = userService.createUser(userName, userRole)

        // Assert
        assertNotNull(result)
        assertEquals(UserName("john."), result?.userName)
        assertEquals(userRole, result?.role)
        verify(exactly = 1) { userRepository.saveUser(any()) }
    }

    @Test
    fun `createUser should increment username extension and return the username with the updated extension when successful`() {
        // Arrange
        val userName = "john.doe"
        val userRole = UserRole.MANAGER
        val newUsername = "john.1"

        every { userRepository.saveUser(any()) } returns User(userName = UserName(newUsername), role = userRole)
        every { transactionManager.getTransaction(any()) } returns mockk<TransactionStatus>()
        every { transactionManager.commit(any()) } just Runs
        every { userRepository.getUserByUsername("john.") } returns User(
            userName = UserName(userName),
            role = userRole
        )
        every { userRepository.getUserByUsername("john.1") } returns null // Replace null with the expected result

        // Act
        val result = userService.createUser(userName, userRole)

        // Assert
        assertNotNull(result)
        assertEquals(newUsername, result?.userName?.value)
        assertEquals(userRole, result?.role)
        verify(exactly = 1) { userRepository.saveUser(any()) }
    }

    @Test
    fun `createUser should set default role when role is null`() {
        // Arrange
        val userName = "john.doe"

        every { userRepository.saveUser(any()) } returns User(userName = UserName(userName), role = defaultUserRole)
        every { transactionManager.getTransaction(any()) } returns mockk<TransactionStatus>()
        every { transactionManager.commit(any()) } just Runs
        every { userRepository.getUserByUsername("john.") } returns null

        // Act
        val result = userService.createUser(userName, null)

        // Assert
        assertNotNull(result)
        assertEquals(UserRole.TEAM_MEMBER, result?.role)
        verify(exactly = 1) { userRepository.saveUser(any()) }
    }

    @Test
    fun `createUser should throw IllegalArgumentException when userName is blank`() {
        // Arrange
        val userName = ""
        every { transactionManager.getTransaction(any()) } returns mockk<TransactionStatus>()

        // Act & Assert
        assertThrows(IllegalArgumentException::class.java) {
            userService.createUser(userName, defaultUserRole)
        }
        verify(exactly = 0) { userRepository.saveUser(any()) }
    }

    @Test
    fun `createUser should throw IllegalArgumentException when userName is null`() {
        // Arrange
        val userName: String? = null

        every { transactionManager.getTransaction(any()) } returns mockk<TransactionStatus>()

        // Act & Assert
        assertThrows(IllegalArgumentException::class.java) {
            userService.createUser(userName, defaultUserRole)
        }
        verify(exactly = 0) { userRepository.saveUser(any()) }
    }

    @Test
    fun `createUser should rollback transaction and rethrow exception when an error occurs`() {
        // Arrange
        val userName = "john.doe"

        every { userRepository.saveUser(any()) } throws RuntimeException("Mocked exception")
        every { transactionManager.getTransaction(any()) } returns mockk<TransactionStatus>()
        every { transactionManager.rollback(any()) } just Runs

        // Act & Assert
        assertThrows(RuntimeException::class.java) {
            userService.createUser(userName, defaultUserRole)
        }
        verify(exactly = 1) { transactionManager.rollback(any()) }
    }
}