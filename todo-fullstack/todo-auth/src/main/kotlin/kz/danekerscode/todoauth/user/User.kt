package kz.danekerscode.todoauth.user

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "users")
class User {
    @Id
    var id: UUID? = null
    var email: String? = null
    var password: String? = null
    var roles: List<String>? = null

}