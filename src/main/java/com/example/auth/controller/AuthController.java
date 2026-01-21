package com.example.auth.controller;

import com.example.auth.io.AuthReq;
import com.example.auth.io.AuthRes;
import com.example.auth.service.AuthUserService;
import com.example.auth.utils.JwtUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthUserService authUserService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login( @RequestBody AuthReq authReq){
        try{
            authenticateUser(authReq.getEmail(),authReq.getPassword());
            UserDetails userDetails = authUserService.loadUserByUsername(authReq.getEmail());
            System.out.println(userDetails);
        String token = jwtUtil.generateToken(userDetails);
            ResponseCookie cookie = ResponseCookie
                    .from("auth",token)
                    .httpOnly(true)
                    .maxAge(Duration.ofDays(1))
                    .path("/")
                    .sameSite("Strict")
                    .build();
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,cookie.toString()).body(new AuthRes(authReq.getEmail(),token));

        }catch (BadCredentialsException exception){
            Map<String,Object> error = new HashMap<>();
            error.put("error",true);
            error.put("message","Required" );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }catch (DisabledException exception){
            Map<String,Object> error = new HashMap<>();
            error.put("error",true);
            error.put("message","Account disabled" );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }catch (Exception exception){
            Map<String,Object> error = new HashMap<>();
            error.put("error",true);
            error.put("message",exception );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    private void authenticateUser( String email, String password) {
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email,password));
    }

}
