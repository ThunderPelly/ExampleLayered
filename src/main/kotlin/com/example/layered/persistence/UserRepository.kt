package com.example.layered.persistence

import com.example.layered.model.User
import org.springframework.stereotype.Repository

@Repository
class UserRepository {
    private var users: MutableList<User> = ArrayList()

    fun getUserByUsername(username: String?): User? = users.find { it.userName == username }

    @Synchronized
    fun saveUser(user: User): User {
        users.add(user)
        return user
    }

    val allUsers: List<User>
        get() = users
}