package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.database.Room;
import com.example.demo.database.RoomRepository;


@Service
public class RoomService {
	
	 @Autowired
	 RoomRepository repository; 
	 
	 public Iterable<Room> findAllLokacije() {
	        return repository.findAll();
	    }
	 

}
