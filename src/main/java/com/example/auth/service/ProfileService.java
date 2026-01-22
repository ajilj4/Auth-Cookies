package com.example.auth.service;

import com.example.auth.io.ProfileReq;
import com.example.auth.io.ProfileRes;

public interface ProfileService {

    ProfileRes createProfile(ProfileReq profileReq);

    ProfileRes getProfile(String email);

    String resetPasswordOtp(String email);

    ;
}
