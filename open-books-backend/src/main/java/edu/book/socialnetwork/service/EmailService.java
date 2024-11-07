package edu.book.socialnetwork.service;

import edu.book.socialnetwork.enums.EmailTemplateName;
import jakarta.mail.MessagingException;

public interface EmailService {
    void sendEmail(String to, String username, EmailTemplateName emailTemplateName, String conformationUrl, String activationCode, String subject) throws MessagingException;
}
