package com.kakakpo.ojk_online_server.user.controller

import com.kakakpo.ojk_online_server.base.entity.BaseResponse
import com.kakakpo.ojk_online_server.user.entity.*
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
@RequestMapping("api_v1/user/customer")
class UserCustomerController {

    @Autowired
    private lateinit var userService: UserService

    @GetMapping
    fun getUser(): BaseResponse<User>{
        val userId = SecurityContextHolder.getContext().authentication.principal as String
        return userService.getUserById(userId).toResponse()
    }

    @PostMapping("/login")
    fun loginAsCustomer(
            @RequestBody userLogin: UserLogin
    ): BaseResponse<LoginResponse > {
        return userService.userLogin(userLogin).toResponse()
    }

    @PostMapping("/register")
    fun registerAsCustomer(
            @RequestBody userRequest: UserRequest
    ): BaseResponse<Boolean> {
        val registerAsCustomer = User.createUser(
            username = userRequest.username,
            password = userRequest.password,
            roles = Roles.CUSTOMER
        )
        return userService.registerUser(userRequest.mapToNewUser()).toResponse()
    }
}