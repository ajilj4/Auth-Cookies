package com.example.auth.io;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileRes {

    private String userId;
    private String name;
    private String email;
    private Boolean isAccountVerified;
}
