package com.kakakpo.ojk_online_server.user.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import java.util.UUID

data class User(
	val id: String = "",
	val username: String = "",
	val password: String = ""
) {
	companion object {

		fun createUser(username: String, password: String): User {
			return User(
				id = UUID.randomUUID().toString(),
				username = username,
				password = password
			)
		}
	}
}
