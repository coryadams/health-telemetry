package com.heartpass.healthtelemetry.controller;

import com.heartpass.healthtelemetry.domain.HealthEvent;
import com.heartpass.healthtelemetry.domain.HealthEventsRequest;
import com.heartpass.healthtelemetry.entity.Activity;
import com.heartpass.healthtelemetry.entity.UserProfile;
import com.heartpass.healthtelemetry.repository.ActivityRepo;
import com.heartpass.healthtelemetry.repository.HealthMetricsCHRepo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

@Controller
@Slf4j
@Scope("session")
public class ActivityController {

    @Autowired
    HealthMetricsCHRepo healthMetricsCHRepo;

    @Autowired
    ActivityRepo activityRepo;

    /**
     * List all for a user
     * @param request
     * @return
     */
    @GetMapping("/activities/")
    public String retrieveActivities(HttpServletRequest request, Model model) {
        UserProfile userProfile = (UserProfile)request.getSession().getAttribute("userProfile");
        log.info("Retrieving all health activities for userProfile.userId: {}.", userProfile.getUserId());

        ArrayList<Activity> activities = activityRepo.findByUserProfileId(userProfile.getId());
        model.addAttribute("activities", activities);
        return "/activities.html";
    }

    @GetMapping("/activities")
    public String retrieveActivityDetails(HttpServletRequest request,
           @RequestParam Integer id, Model model) {
        UserProfile userProfile = (UserProfile)request.getSession().getAttribute("userProfile");
        log.info("Retrieving health metrics for userProfile.userId: {}, activityId: {}", userProfile.getUserId(), id);

        Activity activity = activityRepo.findById(id).orElse(null);

        ArrayList<HealthEvent> events = healthMetricsCHRepo.findByUserProfileIdAndActivityId(userProfile.getId(), id);
        model.addAttribute("healthEvents", events);
        model.addAttribute("activity", activity);
        return "activitydetails.html";
    }
}
