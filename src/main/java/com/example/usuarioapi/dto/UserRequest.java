package com.example.usuarioapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {
    @NotBlank
    @Size(max = 100)
    private String username;

    @NotBlank
    @Email(message = "Invalid email format")
    @Size(max = 150)
    private String email;

    private Boolean active;
}
