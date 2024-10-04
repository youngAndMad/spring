package kz.danekerscode.todo.features.mail

enum class MailMessageType(
    var templateName: String,
    var subject: String,
) {
    EMAIL_CONFIRMATION("mail/email_confirmation.kte", "Email Confirmation"),
}