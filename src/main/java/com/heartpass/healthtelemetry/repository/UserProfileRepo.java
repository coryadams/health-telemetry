package com.heartpass.healthtelemetry.repository;

import com.heartpass.healthtelemetry.entity.UserProfile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepo extends CrudRepository<UserProfile, Integer> {

    UserProfile findByUserId(String userId);
}
