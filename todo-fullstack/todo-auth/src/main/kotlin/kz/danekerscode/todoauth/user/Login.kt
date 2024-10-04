package kz.danekerscode.todoauth.user

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.net.InetAddress
import java.time.Instant

// generate separate model for login?
@Document
class Login {
    @Id
    var id: String? = null
    var time: Instant? = null
    var success: Boolean = false
    var email: String? = null
    var ip: InetAddress? = null
//    var fingerPrint: String? = null todo add in future
}