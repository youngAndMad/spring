package kz.danekerscode.todo.features.user.internal

import kz.danekerscode.todo.features.user.User
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository : MongoRepository<User, String> {

    fun existsByEmail(email: String): Boolean

}