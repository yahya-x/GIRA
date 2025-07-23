package com.GIRA.Backend.DTO.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for user login requests.
 * <p>
 * Contains the credentials required for user authentication.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * Email of the user. Must be valid and not blank.
     */
    @JsonProperty("email")
    @Email
    @NotBlank
    private String email;

    /**
     * Password for the user account. Cannot be blank.
     */
    @JsonProperty("password")
    @NotBlank
    private String password;
} 