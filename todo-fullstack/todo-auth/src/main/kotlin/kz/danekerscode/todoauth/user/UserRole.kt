package kz.danekerscode.todoauth.user

enum class UserRole {
    /**
     * Common user role
     * */
    USER,
    /**
     * Admin user role
     * */
    ADMIN,
    /**
     * Guest user role, (registered but not confirmed)
     * */
    GUEST
}