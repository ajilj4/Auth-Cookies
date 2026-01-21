package com.example.auth.service;

import com.example.auth.entity.UserEntity;
import com.example.auth.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AuthUserService implements UserDetailsService {

    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity existingUser = userRepo.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException("Email not fount"+email));
        return new User(existingUser.getEmail(),
                existingUser.getPassword(),
                new ArrayList<>());
    }

}
