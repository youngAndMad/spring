package kz.danekerscode.todo.features.auth.internal

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import kz.danekerscode.todo.features.auth.AuthService
import kz.danekerscode.todo.features.auth.RegisterRequest
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authService: AuthService
) {

    @Operation(
        summary = "Registration", responses = [
            ApiResponse(responseCode = "200", description = "Success. Email confirmation sent"),
        ]
    )
    @PostMapping("/register")
    fun register(
        @RequestBody registerRequest: RegisterRequest
    ) = authService.register(registerRequest)


}