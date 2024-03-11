package com.example.food_rec.controller;

import com.example.food_rec.Service.UserService;
import com.example.food_rec.domain.Storge;
import com.example.food_rec.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;


@Controller
public class UserController {

@Autowired
UserService userService;

    //登录
    @RequestMapping("/login")
    public String login(){
        return "login";
    }
    @GetMapping("/result")
    public String result(HttpServletRequest req){

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        Storge.setUserid(username);
        User user = new User(username,password);

        boolean flag = userService.login(user);

        if(flag==true){
            return "buffering";
        }else {
            return "login";
        }
    }



}
