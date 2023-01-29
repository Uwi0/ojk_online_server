package com.kakakpo.ojk_online_server.user.repository

import com.kakakpo.ojk_online_server.user.entity.User

interface UserRepository {

    fun insertUser(user: User): Result<Boolean>

    fun getUserById(id: String): Result<User>

    fun getUserByUsername(username: String): Result<User>
}