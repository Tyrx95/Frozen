package com.example.demo.mvc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.database.Room;
import com.example.demo.service.RoomService;

@RestController
@CrossOrigin
@RequestMapping(path="/room")
public class RoomController {
	
	@Autowired
	RoomService roomServce;
	
	@RequestMapping(path="/all", method = RequestMethod.GET)
    public Iterable<Room> findAll() {
		return roomServce.findAllLokacije();
    	}
	 
}
