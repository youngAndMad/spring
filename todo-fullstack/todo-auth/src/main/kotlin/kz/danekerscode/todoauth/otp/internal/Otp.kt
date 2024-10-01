package kz.danekerscode.todoauth.otp.internal

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
class Otp {
    @Id
    var id: String? = null
    var value: String? = null
    var email: String? = null
    var expirationTime: LocalDateTime? = null
    var used: Boolean = false
    val createdTime: LocalDateTime = LocalDateTime.now()
}