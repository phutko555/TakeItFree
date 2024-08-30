package com.TakeItFree.TakeItFree.Web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/")
    public String rootView () {
        return "index";
    }

    @GetMapping("/dashboard")
   public String dashboardPage(){
        return "dashboard";
    }
}