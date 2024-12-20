package edu.book.socialnetwork.domain.user.controller;

import edu.book.socialnetwork.domain.user.dto.request.AuthenticationRequest;
import edu.book.socialnetwork.domain.user.dto.request.ChangePasswordRequest;
import edu.book.socialnetwork.domain.user.dto.request.RegistrationRequest;
import edu.book.socialnetwork.domain.user.dto.response.AuthenticationResponse;
import edu.book.socialnetwork.domain.user.service.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication endpoints")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Void> register(@RequestBody @Valid RegistrationRequest request) throws MessagingException {
        authenticationService.register(request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody @Valid AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @GetMapping("activate-account")
    public void confirm(@RequestParam String token) throws MessagingException {
        authenticationService.activateAccount(token);
    }

    @PatchMapping("/change-password" + "/{id}")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request, Authentication connectedUser) {
        authenticationService.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }

}
