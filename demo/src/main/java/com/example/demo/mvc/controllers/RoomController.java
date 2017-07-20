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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.database.Room;
import com.example.demo.service.RoomService;


@Controller
public class RoomController {
	
	
	
	private RoomService roomService;
	

    @Autowired
    public void setRoomService(RoomService roomService) {
        this.roomService = roomService;
    }
	
	@GetMapping("/admin")
	public String countsList(Model model) {
	    model.addAttribute("rooms",roomService.listAll());
	    return "admin";
	}
	
	  @RequestMapping(value = "/add", method = RequestMethod.POST)
	    public ResponseEntity saveRoom(@RequestBody Room room){
	        roomService.add(room);
	        return new ResponseEntity("Product saved successfully", HttpStatus.OK);
	    }

	
	 
}