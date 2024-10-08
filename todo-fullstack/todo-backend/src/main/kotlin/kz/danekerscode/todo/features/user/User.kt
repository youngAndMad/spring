package kz.danekerscode.todo.features.user

import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "users")
class User {
    var id: String? = null
    var email: String? = null
    var password: String? = null
    var emailConfirmed: Boolean = false
    var role: List<UserRole> = mutableListOf(UserRole.GUEST)
}