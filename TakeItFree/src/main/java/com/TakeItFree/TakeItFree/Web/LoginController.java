package com.TakeItFree.TakeItFree.Web;

import com.TakeItFree.TakeItFree.Service.UserService;
import com.TakeItFree.TakeItFree.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/register")
    public String register(ModelMap model){
        model.put("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerPost(@ModelAttribute User user, @RequestParam("role") String role){

        User savedUser = userService.save(user, role);

        System.out.println("Non-saved user:" + user);
        System.out.println("Saved user: " + savedUser);

        return "redirect:/login";
    }
}









