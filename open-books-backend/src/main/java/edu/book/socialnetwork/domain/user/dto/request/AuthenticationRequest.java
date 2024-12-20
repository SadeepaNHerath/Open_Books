package edu.book.socialnetwork.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationRequest {

    @Email(message = "Invalid email format")
    @NotEmpty(message = "Email cannot be empty")
    @NotBlank(message = "Email is mandatory")
    private String email;

    @NotEmpty(message = "Password cannot be empty")
    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;
}
