package kz.danekerscode.todo.features.mail

enum class MailMessageType(
    var templateName: String,
    var subject: String,
) {
    EMAIL_CONFIRMATION("email_confirmation.html", "Email Confirmation");
}