package kz.danekerscode.todo.features.user

import kz.danekerscode.todo.features.user.internal.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository
) {

    fun existsByEmail(email: String): Boolean = userRepository.existsByEmail(email)

    fun save(user: User): User = userRepository.save(user)
}