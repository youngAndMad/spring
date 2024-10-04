package kz.danekerscode.todoauth.mail

data class SendMailMessage(
    val receiver: String,
    val type: MailMessageType,
    val data: Map<String, Any>,
)