package com.heartpass.healthtelemetry.controller;

import com.heartpass.healthtelemetry.entity.UserProfile;
import com.heartpass.healthtelemetry.service.UserProfileService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Scope("session")
public class LoginController {

    @Autowired
    UserProfileService userProfileService;

    @GetMapping({"/login.html"})
    public String login() {
        return "/login";
    }

    /**
     * userId will be loaded into the Session
     *
     * @param request
     * @param userId
     * @param password
     * @param model
     * @return
     */
    @PostMapping({"/login"})
    public String processLogin(HttpServletRequest request, @RequestParam String userId, @RequestParam String password, Model model) {
        UserProfile userProfile;
        userProfile = userProfileService.retrieve(userId);
        if (userProfile != null && !userProfile.getPassword().equals(password)) {
            model.addAttribute("authError", "true");
            return "/login";
        } else if (userProfile != null) {
            request.getSession().setAttribute("userProfile", userProfile);
            return "/landing.html";
        }
        return "/login";
    }

    @PostMapping({"/logout"})
    public String processLogout(HttpServletRequest request, @RequestParam String userId, Model model) {
        request.getSession().removeAttribute("userProfile");
        return "/login.html";
    }
}
