package com.heartpass.healthtelemetry.controller;

import com.heartpass.healthtelemetry.entity.UserProfile;
import com.heartpass.healthtelemetry.service.UserProfileService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    UserProfileService userProfileService;

    @GetMapping({"/login.html"})
    public String login() {
        return "/login";
    }

    /**
     * userId will be loaded into the Session
     * @param request
     * @param userId
     * @param password
     * @param model
     * @return
     */
    @PostMapping({"/login"})
    public String processLogin(HttpServletRequest request,  @RequestParam String userId, @RequestParam String password, Model model) {
        UserProfile userProfile;
        userProfile = userProfileService.retrieve(userId);
        if (userProfile != null) {
            request.getSession().setAttribute("userId", userProfile.getUserId());
            model.addAttribute("firstName", userProfile.getFirstName());
            return "/landing.html";
        } else {
            model.addAttribute("authError", "Authentication failed");
            return "/login";
        }
    }
}
