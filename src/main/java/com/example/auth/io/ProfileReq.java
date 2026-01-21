package com.example.auth.io;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileReq {

    @NotBlank(message = "Name should not be empty")
    private String name;
    @Email
    @NotNull(message = "Email shount not be empty")
    private String email;
    @Size(min = 6,message = "MIn 6 char req")
    private String password;
}
