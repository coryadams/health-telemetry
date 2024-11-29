package com.heartpass.healthtelemetry.service;

import com.heartpass.healthtelemetry.entity.UserProfile;
import com.heartpass.healthtelemetry.repository.UserProfileRepo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {

    @Autowired
    UserProfileRepo userProfileRepo;

    @PersistenceContext
    private EntityManager entityManager;

    public UserProfile save(UserProfile userProfile) {
        return userProfileRepo.save(userProfile);
    }

    public UserProfile retrieve(String userId) {
        entityManager.clear();
        UserProfile userProfile = userProfileRepo.findByUserId(userId);
        // Detach the object so as not to receive ConcurrentModificationException
        if (userProfile != null) {
            entityManager.detach(userProfile);
        }
        return userProfile;
    }
}
