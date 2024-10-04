package kz.danekerscode.todo.features.otp.internal

import org.springframework.data.mongodb.repository.MongoRepository

interface OtpRepository : MongoRepository<Otp, String> {
    fun findByEmailAndUsedIsFalse(email: String): Otp?

}