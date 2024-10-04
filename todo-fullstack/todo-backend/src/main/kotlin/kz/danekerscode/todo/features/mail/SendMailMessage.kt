package kz.danekerscode.todo.features.mail

data class SendMailMessage(
    val receiver: String,
    val type: MailMessageType,
    val data: Map<String, Any>,
)