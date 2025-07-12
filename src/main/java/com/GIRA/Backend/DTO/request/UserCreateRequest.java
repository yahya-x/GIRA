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
 * DTO for creating a new user.
 * <p>
 * Contains the information required to register a new user account.
 * </p>
 *
 * @author Mohamed yahya jabrane
 * @version 1.0
 * @since 10/07/2025
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * Username of the user. Cannot be blank.
     */
    @JsonProperty("username")
    @NotBlank
    private String username;
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