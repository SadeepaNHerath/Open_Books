package edu.book.socialnetwork.domain.user.service.impl;

import edu.book.socialnetwork.shared.enums.EmailTemplateName;
import edu.book.socialnetwork.domain.user.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine springTemplateEngine;

    @Value("${application.mailing.enabled:true}")
    private boolean mailingEnabled;

    @Value("${application.mailing.from:}")
    private String fromAddress;

    @Value("${spring.mail.username:}")
    private String mailUsername;

    @Value("${spring.mail.password:}")
    private String mailPassword;

    @Override
    @Async
    public void sendEmail(
            String to,
            String username,
            EmailTemplateName emailTemplateName,
            String confirmationUrl,
            String activationCode,
            String subject
    ) throws MessagingException {
        if (to == null || to.isEmpty()) {
            throw new IllegalArgumentException("Recipient email address cannot be null or empty.");
        }

        if (!mailingEnabled) {
            log.warn("Email sending is disabled (application.mailing.enabled=false). Skipping activation email for {}", to);
            return;
        }

        if (!StringUtils.hasText(mailUsername) || !StringUtils.hasText(mailPassword)) {
            log.warn("SMTP credentials are missing. Set OPENBOOKS_MAIL_USERNAME and OPENBOOKS_MAIL_PASSWORD to enable email sending.");
            return;
        }

        String templateName = (emailTemplateName == null) ? "confirmation-email" : emailTemplateName.name();

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED, StandardCharsets.UTF_8.name());

        Map<String, Object> properties = new HashMap<>();
        properties.put("username", username);
        properties.put("confirmationUrl", confirmationUrl);
        properties.put("activation_code", activationCode);

        Context context = new Context();
        context.setVariables(properties);

        helper.setFrom(StringUtils.hasText(fromAddress) ? fromAddress : mailUsername);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(springTemplateEngine.process(templateName, context), true);

        javaMailSender.send(mimeMessage);
    }

}
