package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.database.Room;
import com.example.demo.database.RoomRepository;



public interface RoomService {
	
	 List<Room> listAll();
	 void add(Room room);
	 void delete(Long id);
	 void update(Room room);
	 Room getOne(Long id);
	 
	 
	 
	 
}
