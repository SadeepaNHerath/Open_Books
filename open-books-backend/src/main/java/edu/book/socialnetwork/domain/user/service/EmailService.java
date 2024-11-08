package edu.book.socialnetwork.domain.user.service;

import edu.book.socialnetwork.shared.enums.EmailTemplateName;
import jakarta.mail.MessagingException;

public interface EmailService {
    void sendEmail(String to, String username, EmailTemplateName emailTemplateName, String conformationUrl, String activationCode, String subject) throws MessagingException;
}
