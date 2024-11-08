package edu.book.socialnetwork.domain.user.service;

import edu.book.socialnetwork.domain.user.dto.request.AuthenticationRequest;
import edu.book.socialnetwork.domain.user.dto.request.RegistrationRequest;
import edu.book.socialnetwork.domain.user.dto.response.AuthenticationResponse;
import jakarta.mail.MessagingException;

public interface AuthenticationService {
    void register(RegistrationRequest request) throws MessagingException;

    AuthenticationResponse authenticate(AuthenticationRequest request);

    void activateAccount(String token) throws MessagingException;
}
