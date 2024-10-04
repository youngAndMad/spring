package kz.danekerscode.todo.features.otp.internal

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document
class Otp {
    @Id
    var id: String? = null
    var value: String? = null
    var email: String? = null
    var expirationTime: Instant? = null
    var used: Boolean = false
    val createdTime: Instant = Instant.now()
}