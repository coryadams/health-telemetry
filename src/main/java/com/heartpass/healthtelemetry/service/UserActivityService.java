package com.heartpass.healthtelemetry.service;

import com.heartpass.healthtelemetry.entity.Activity;
import com.heartpass.healthtelemetry.repository.ActivityRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserActivityService {

    @Autowired
    ActivityRepo activityRepo;

    public Activity save(Activity activity) {
        return activityRepo.save(activity);
    }

    public Activity retrieve(Integer id) {
        return activityRepo.findById(id).orElse(null);
    }
}
