package com.kakakpo.ojk_online_server.user.service

import com.kakakpo.ojk_online_server.user.entity.LoginResponse
import com.kakakpo.ojk_online_server.user.entity.User
import com.kakakpo.ojk_online_server.user.entity.UserLogin

interface UserService {
    fun userLogin(userLogin: UserLogin): Result<LoginResponse>
    fun registerUser(user: User): Result<Boolean>
    fun getUserById(id: String): Result<User>
    fun getUserByUsername(username: String): Result<User>
}