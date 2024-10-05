package kz.danekerscode.todo.features.auth

import kz.danekerscode.todo.core.errors.ApiException
import kz.danekerscode.todo.features.mail.MailMessageType
import kz.danekerscode.todo.features.mail.MailService
import kz.danekerscode.todo.features.mail.SendMailMessage
import kz.danekerscode.todo.features.otp.OtpService
import kz.danekerscode.todo.features.user.User
import kz.danekerscode.todo.features.user.UserService
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userService: UserService,
    private val passwordEncoder: PasswordEncoder,
    private val mailService: MailService,
    private val otpService: OtpService
) {

    fun register(registerRequest: RegisterRequest) {
        if (userService.existsByEmail(registerRequest.email)) {
            throw ApiException("Email already taken", HttpStatus.BAD_REQUEST)
        }

        val otp = otpService.generateForEmail(registerRequest.email)

        mailService.sendMail(
            SendMailMessage(
                receiver = registerRequest.email,
                type = MailMessageType.EMAIL_CONFIRMATION,
                data = mutableMapOf("otp" to otp)
            )
        )

        User().apply {
            email = registerRequest.email
            password = passwordEncoder.encode(registerRequest.password)
        }
    }

}