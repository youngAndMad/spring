package kz.danekerscode.todo.features.mail

import gg.jte.TemplateEngine
import gg.jte.output.StringOutput
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets

@Service
class MailService(
    private val templateEngine: TemplateEngine,
    private val mailSender: JavaMailSender,
) {
    val log = KotlinLogging.logger { }

    fun sendMail(
        data: SendMailMessage
    ) {
        val htmlContent = StringOutput()
        templateEngine.render(data.type.templateName, data.data, htmlContent)

        val msg = mailSender.createMimeMessage()

        val helper =
            MimeMessageHelper(
                msg,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name(),
            )

        helper.setText(htmlContent.toString(), true)
        helper.setTo(data.receiver)
        helper.setSubject(data.type.subject)

        mailSender.send(msg)

        log.info { "Mail sent to ${data.receiver}" }
    }

}