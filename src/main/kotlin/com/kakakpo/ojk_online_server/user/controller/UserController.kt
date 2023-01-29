package com.kakakpo.ojk_online_server.user.controller

import com.kakakpo.ojk_online_server.base.entity.BaseResponse
import com.kakakpo.ojk_online_server.user.entity.LoginResponse
import com.kakakpo.ojk_online_server.user.entity.User
import com.kakakpo.ojk_online_server.user.entity.UserLogin
import com.kakakpo.ojk_online_server.user.entity.UserRequest
import com.kakakpo.ojk_online_server.user.service.UserService
import com.kakakpo.ojk_online_server.utils.toResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/user")
class UserController {

    @Autowired
    private lateinit var userService: UserService

    @GetMapping
    fun getUser(): BaseResponse<User>{
        val userId = SecurityContextHolder.getContext().authentication.principal as String
        return userService.getUserById(userId).toResponse()
    }

    @PostMapping("/login")
    fun login(
            @RequestBody userLogin: UserLogin
    ): BaseResponse<LoginResponse > {
        return userService.userLogin(userLogin).toResponse()
    }

    @PostMapping("/register")
    fun register(
            @RequestBody userRequest: UserRequest
    ): BaseResponse<Boolean> {
        return userService.registerUser(userRequest.mapToNewUser()).toResponse()
    }
}