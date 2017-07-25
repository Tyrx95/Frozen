package com.example.demo.mvc.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.database.User;
import com.example.demo.service.UserService;

@Controller
public class UserController {

	private UserService userService;
	

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
	
//	@GetMapping("/admin")
//	public String countsList(Model model) {
//	    model.addAttribute("users",userService.findAll());
//	    return "admin";
//	}
	
//	@RequestMapping(value = "user", method = RequestMethod.POST)
//    public String saveUser(User user){
//        userService.add(user);
//        return "redirect:/admin";
//    }

	@RequestMapping("usershow")
	     public String showUser(Model model){
	 		model.addAttribute("users",userService.findAll());
	         return "users";
	    }
	 
}
