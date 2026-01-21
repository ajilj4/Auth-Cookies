package com.example.auth.service;

import com.example.auth.entity.UserEntity;
import com.example.auth.exception.EmailAlreadyExist;
import com.example.auth.io.ProfileReq;
import com.example.auth.io.ProfileRes;
import com.example.auth.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileServiceImp implements ProfileService{


    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ProfileRes createProfile(ProfileReq request) {
        UserEntity newUser = convertToUserEntity(request);
        if(userRepo.findByEmail(newUser.getEmail()).isPresent()){
            throw new EmailAlreadyExist(newUser.getEmail());
        }
        newUser = userRepo.save(newUser);

        return convertToProfileRes(newUser);
    }

    private UserEntity convertToUserEntity(ProfileReq request) {
        return UserEntity.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .userId(UUID.randomUUID().toString())
                .isAccountVerified(false)
                .resetOpt(null)
                .verifyOtp(null)
                .VerifiedOtpExpiredAt(0L)
                .resetOtpVerifiedAt(0L)
                .build();
    }

    private ProfileRes convertToProfileRes(UserEntity newUser) {
        return ProfileRes.builder()
                .name(newUser.getName())
                .email(newUser.getEmail())
                .userId(newUser.getUserId())
                .isAccountVerified(newUser.getIsAccountVerified())
                .build();
    }
}
