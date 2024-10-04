package kz.danekerscode.todoauth.otp

import io.github.oshai.kotlinlogging.KotlinLogging
import kz.danekerscode.todoauth.otp.internal.Otp
import kz.danekerscode.todoauth.otp.internal.OtpRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.security.SecureRandom
import java.time.Duration
import java.time.Instant

@Service
class OtpService(
    private val otpRepository: OtpRepository,
    @Value("\${todo-auth.otp-expiration:2m}") private val otpExpiration: Duration
) {
    companion object {
        private val random = SecureRandom()
        private val log = KotlinLogging.logger {}
    }

    fun generateForEmail(email: String): String = Otp().apply {
        this.email = email
        this.value = generateOtp()
        this.expirationTime = Instant.now().plus(otpExpiration)
        log.info { "Generated OTP ****** for email $email" }
    }.let { otpRepository.save(it) }.value!!

    /**
     * Генерирует OTP в виде строки из 6 цифр
     * */
    private fun generateOtp() = StringBuilder().apply {
        repeat(6) {
            append(random.nextInt(10))
        }
    }.toString()
}
