package com.heartpass.healthtelemetry.controller;

import com.heartpass.healthtelemetry.entity.UserProfile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;

@Controller
public class AccountController {

    @GetMapping({"/create_account.html"})
    public String createAccount(Model model) {
        UserProfile userProfile = new UserProfile();
        model.addAttribute("userProfile", userProfile);
        return "/create_account";
    }

    @PostMapping({"/create_account"})
    public String saveAccount(Model model, @ModelAttribute("userProfile") UserProfile userProfile)
    {
        userProfile.setCreatedAt(LocalDateTime.now());
        userProfile.setUpdatedAt(LocalDateTime.now());

        // Save it

        model.addAttribute("userProfile", userProfile);
        return "/landing";
    }
}
