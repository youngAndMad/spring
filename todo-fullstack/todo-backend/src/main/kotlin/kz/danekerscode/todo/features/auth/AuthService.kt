package kz.danekerscode.todo.features.auth

import kz.danekerscode.todo.features.user.UserService
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userService: UserService,
) {

    fun register(registerRequest: RegisterRequest) {
        if (userService.existsByEmail(registerRequest.email)) {
            throw IllegalStateException("Email already taken")
        }
    }

}