package edu.book.socialnetwork.domain.user.service.impl;

import edu.book.socialnetwork.domain.user.dto.request.AuthenticationRequest;
import edu.book.socialnetwork.domain.user.dto.request.RegistrationRequest;
import edu.book.socialnetwork.domain.user.dto.response.AuthenticationResponse;
import edu.book.socialnetwork.domain.user.entity.RoleEntity;
import edu.book.socialnetwork.domain.user.entity.TokenEntity;
import edu.book.socialnetwork.domain.user.entity.UserEntity;
import edu.book.socialnetwork.shared.enums.EmailTemplateName;
import edu.book.socialnetwork.domain.user.repository.RoleRepository;
import edu.book.socialnetwork.domain.user.repository.TokenRepository;
import edu.book.socialnetwork.domain.user.repository.UserRepository;
import edu.book.socialnetwork.domain.user.service.AuthenticationService;
import edu.book.socialnetwork.domain.user.service.EmailService;
import edu.book.socialnetwork.domain.user.service.JwtService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;

    private void sendValidationEmail(UserEntity user) throws MessagingException {
        String newToken = generateAndSaveActivationToken(user);
        emailService.sendEmail(
                user.getEmail(),
                user.fullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Account activation"
        );

    }

    @Override
    public void register(RegistrationRequest request) throws MessagingException {
        RoleEntity userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("User role not found"));
        UserEntity user = UserEntity.builder()
                .firstname(request.getFirstName())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(userRole))
                .build();
        userRepository.save(user);
        sendValidationEmail(user);

    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        HashMap<String, Object> claims = new HashMap<>();
        UserEntity user = (UserEntity) auth.getPrincipal();
        claims.put("fullName", user.fullName());
        String jwtToken = jwtService.generateToken(claims, user);

        return AuthenticationResponse.builder()
                .token(jwtToken).build();
    }

    @Override
    @Transactional
    public void activateAccount(String token) throws MessagingException {
        TokenEntity savedToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));
        if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            sendValidationEmail(savedToken.getUser());
            throw new RuntimeException("Activation token has expired. A new token has been send to your email.");
        }
        UserEntity user = userRepository.findById(savedToken.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found."));
        user.setEnabled(true);
        userRepository.save(user);
        savedToken.setValidatedAt(java.time.LocalDateTime.now());
        tokenRepository.save(savedToken);
    }

    private String generateAndSaveActivationToken(UserEntity user) {
        String generatedToken = generateActivationToken(6);
        TokenEntity token = TokenEntity.builder()
                .token(generatedToken)
                .createdAt(java.time.LocalDateTime.now())
                .expiresAt(java.time.LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        tokenRepository.save(token);
        return generatedToken;
    }

    private String generateActivationToken(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }
        return codeBuilder.toString();
    }
}
