package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.database.Room;
import com.example.demo.database.RoomRepository;


@RestController
public class WebController {
	@Autowired
	RoomRepository repository;
	
	@RequestMapping("/save")
	public String process(){
		repository.save(new Room("853", "Smith"));
		repository.save(new Room("485", "LKA"));
		repository.save(new Room("85", "GH"));
		repository.save(new Room("55", "KO"));
		repository.save(new Room("85834503485", "Sasd"));
		return "Done";
	}
	
	@RequestMapping("/findall")
	public String findAll(){
		String result = "<html>";
		
		for(Room room : repository.findAll()){
			result += "<div>" + room.toString() + "</div>";
		}
		
		return result + "</html>";
	}
	
	@RequestMapping("/findbyid")
	public String findById(@RequestParam("id") long id){
		String result = "";
		result = repository.findOne(id).toString();
		return result;
	}
	
	@RequestMapping("/findbyname")
	public String fetchDataByLastName(@RequestParam("name") String name){
		String result = "<html>";
		
		for(Room room: repository.findByName(name)){
			result += "<div>" + room.toString() + "</div>"; 
		}
		
		return result + "</html>";
	}
}