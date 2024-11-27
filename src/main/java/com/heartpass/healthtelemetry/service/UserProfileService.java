package com.heartpass.healthtelemetry.service;

import com.heartpass.healthtelemetry.entity.UserProfile;
import com.heartpass.healthtelemetry.repository.UserProfileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {

    @Autowired
    UserProfileRepo userProfileRepo;

    public UserProfile save(UserProfile userProfile) {
        return userProfileRepo.save(userProfile);
    }

    public UserProfile retrieve(String userId) {
        return userProfileRepo.findByUserId(userId);
    }
}
