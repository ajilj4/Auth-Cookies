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
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class ProfileServiceImp implements ProfileService{


    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    public ProfileRes createProfile(ProfileReq request) {
        UserEntity newUser = convertToUserEntity(request);
        if(userRepo.findByEmail(newUser.getEmail()).isPresent()){
            throw new EmailAlreadyExist(newUser.getEmail());
        }
        newUser = userRepo.save(newUser);

        try{
            emailService.welcomeMail(newUser.getEmail(), newUser.getName());
        }catch (Exception e){
            System.out.println("email error : "+e.getMessage());
        }

        return convertToProfileRes(newUser);
    }

    @Override
    public ProfileRes getProfile(String email) {

        UserEntity user = userRepo.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException("User not fount"));

        return convertToProfileRes(user);
    }

    @Override
    public String resetPasswordOtp(String email) {
        try{
            System.out.println("email"+email);
            UserEntity existingUser = userRepo.findByEmail(email).orElseThrow();
            String otp = String.valueOf(ThreadLocalRandom.current().nextInt(100000,1000000));
            Long expireTime = System.currentTimeMillis() + (5*60*1000);

            existingUser.setResetOpt(otp);
            existingUser.setResetOtpVerifiedAt(expireTime);
            userRepo.save(existingUser);

            emailService.sendResetPassOtp(existingUser.getEmail(),otp);

            return "Otp Successfully send to your email address";

        }catch (Exception e){
            return "something went wrong ";
        }


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
