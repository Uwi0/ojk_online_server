package com.kakakpo.ojk_online_server.user.service

import com.kakakpo.ojk_online_server.authentication.JWTConfig
import com.kakakpo.ojk_online_server.exception.OjkException
import com.kakakpo.ojk_online_server.user.entity.LoginResponse
import com.kakakpo.ojk_online_server.user.entity.User
import com.kakakpo.ojk_online_server.user.entity.UserLogin
import com.kakakpo.ojk_online_server.user.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class OjkOnlineUserService(
	@Autowired
	private val userRepository: UserRepository
) : UserService {
	override fun userLogin(userLogin: UserLogin): Result<LoginResponse> {
		val result = userRepository.getUserByUsername(userLogin.username)
		return result.map { user ->
			val token = JWTConfig.generateToken(user)
			if (passwordMatch(userLogin, user)) {
				LoginResponse(token)
			} else {
				throw OjkException("Password invalid")
			}
		}
	}

	override fun registerUser(user: User): Result<Boolean> {
		return userRepository.insertUser(user)
	}

	override fun getUserById(id: String): Result<User> {
		return userRepository.getUserById(id)
	}

	override fun getUserByUsername(username: String): Result<User> {
		return userRepository.getUserByUsername(username)
	}

	private fun passwordMatch(userLogin: UserLogin, user: User): Boolean {
		return userLogin.password == user.password
	}
}