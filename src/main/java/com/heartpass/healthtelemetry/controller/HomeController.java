package com.heartpass.healthtelemetry.controller;

import com.heartpass.healthtelemetry.domain.FileCreateRequest;
import com.heartpass.healthtelemetry.entity.UserProfile;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping({"/", "/index.html"})
    public String index(HttpServletRequest request) {
        UserProfile userProfile = (UserProfile) request.getSession().getAttribute("userProfile");
        if (userProfile == null) {
            return "/login.html";
        }
        return "/index";
    }

    /**
     *
     * @param request - Used so that the Session attributes are carried to the view
     * @return
     */
    @GetMapping({"file_upload.html"})
    public String fileUpload(HttpServletRequest request, Model model) {
        String userId = (String) request.getSession().getAttribute("userId");
        model.addAttribute("userId", userId);
        model.addAttribute("fileCreateRequest", new FileCreateRequest());
        return "/file_upload";
    }
}
