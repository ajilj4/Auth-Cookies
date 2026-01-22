package com.example.auth.controller;

import com.example.auth.entity.UserEntity;
import com.example.auth.io.ProfileReq;
import com.example.auth.io.ProfileRes;
import com.example.auth.service.ProfileServiceImp;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ProfileController {


    private final ProfileServiceImp profileServiceImp;

    @GetMapping("/test")
    public String test(){
       return  "success";
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ProfileRes createUser(
            @Valid
            @RequestBody ProfileReq req){
        return profileServiceImp.createProfile(req);
    }

    @GetMapping("/profile")
    public ProfileRes getProfile(@CurrentSecurityContext(expression = "authentication.name") String email){
      return profileServiceImp.getProfile(email);
    }
}
