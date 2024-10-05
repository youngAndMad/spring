package kz.danekerscode.todo.features.mail

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import java.nio.charset.StandardCharsets

@Service
class MailService(
    private val templateEngine: TemplateEngine,
    private val mailSender: JavaMailSender,
) {
    val log = KotlinLogging.logger { }

    fun sendMail(
        mailData: SendMailMessage
    ) {

        val context = Context()
        context.setVariable("receiver", mailData.receiver)
        mailData.data.forEach { context.setVariable(it.key, it.value) }

        val htmlContent = templateEngine.process(mailData.type.templateName, context)

        val msg = mailSender.createMimeMessage()
        val helper =
            MimeMessageHelper(
                msg,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name(),
            )

        helper.setText(htmlContent, true)
        helper.setTo(mailData.receiver)
        helper.setSubject(mailData.type.subject)

        // mailSender.send(msg)

        log.info { "Mail sent to ${mailData.receiver}" }
    }
}
