package kz.danekerscode.todo.core.errors

import org.springframework.http.HttpStatus

data class ApiException(
    override val message: String,
    val code: HttpStatus,
) : RuntimeException(message)
