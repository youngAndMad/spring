package kz.danekerscode.todoauth.otp

import io.github.oshai.kotlinlogging.KotlinLogging
import kz.danekerscode.todoauth.otp.internal.Otp
import kz.danekerscode.todoauth.otp.internal.OtpRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.security.SecureRandom
import java.time.Duration
import java.time.LocalDateTime

@Service
class OtpService(
    private val otpRepository: OtpRepository,
    @Value("\${todo-auth.otp-expiration:#{'2m'}") private val otpExpiration: Duration
) {
    val random = SecureRandom()
    val log = KotlinLogging.logger {}

    fun generateForEmail(email: String): String = Otp().apply {
        this.email = email
        this.value = generateOtp()
        this.expirationTime = LocalDateTime.now().plus(otpExpiration)
        log.info { "Generated OTP ****** for email $email" }
    }.let { otpRepository.save(it) }.value!!

    private fun generateOtp() = random.nextInt(100_000, 999_999).toString()

}