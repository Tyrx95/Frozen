package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.database.User;
import com.example.demo.database.UserRepository;



public interface UserService {
	
	 List<User> findAll();
	 void add(User user);
	 void delete(Long id);
	 User getByViberId(String viberId);
	 void subscribe(String viberId);
	 void unsubscribe(String viberId);
	 
	 
	 
	 
}
