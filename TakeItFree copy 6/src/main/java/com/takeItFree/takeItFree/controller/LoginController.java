package com.takeItFree.takeItFree.controller;

import com.takeItFree.takeItFree.service.UserService;
import com.takeItFree.takeItFree.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/register")
    public String register(ModelMap model){
        return "register";
    }
}









