package edu.book.socialnetwork.service;

import edu.book.socialnetwork.dto.request.AuthenticationRequest;
import edu.book.socialnetwork.dto.request.RegistrationRequest;
import edu.book.socialnetwork.dto.response.AuthenticationResponse;
import jakarta.mail.MessagingException;

public interface AuthenticationService {
    void register(RegistrationRequest request) throws MessagingException;

    AuthenticationResponse authenticate(AuthenticationRequest request);

    void activateAccount(String token) throws MessagingException;
}
