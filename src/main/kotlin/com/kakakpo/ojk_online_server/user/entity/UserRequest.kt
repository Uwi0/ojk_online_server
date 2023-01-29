package com.kakakpo.ojk_online_server.user.entity

data class UserRequest(
	val username: String,
	val password: String
){
	fun mapToNewUser(): User{
		return User.createUser(username, password)
	}
}
