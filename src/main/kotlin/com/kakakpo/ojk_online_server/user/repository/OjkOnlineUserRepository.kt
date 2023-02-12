package com.kakakpo.ojk_online_server.user.repository

import com.kakakpo.ojk_online_server.DatabaseComponent
import com.kakakpo.ojk_online_server.exception.OjkException
import com.kakakpo.ojk_online_server.user.entity.Roles
import com.kakakpo.ojk_online_server.user.entity.User
import com.kakakpo.ojk_online_server.utils.orThrow
import com.kakakpo.ojk_online_server.utils.toResult
import com.mongodb.client.MongoCollection
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.getCollection
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.lang.IllegalArgumentException

@Repository
class OjkOnlineUserRepository(
        @Autowired
        private val databaseComponent: DatabaseComponent
) : UserRepository {

    private fun getCollection(): MongoCollection<User> =
            databaseComponent.database.getDatabase(DatabaseComponent.DATABASE_NAME).getCollection()

    override fun insertUser(user: User): Result<Boolean> {
        val existingUser = getUserByUsername(user.username)
        return if (existingUser.isSuccess) {
            throw OjkException("User exist")
        } else {
            getCollection().insertOne(user).wasAcknowledged().toResult()
        }
    }

    override fun getUserById(id: String): Result<User> {
        return getCollection().findOne(User::id eq id).toResult()
    }

    override fun getUserByUsername(username: String): Result<User> {
        return getCollection().findOne(User::username eq username).toResult("user with $username not found")
    }
}