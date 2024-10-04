package kz.danekerscode.todo.features.auth

data class RegisterRequest(
    val email: String,
    val password: String
)
