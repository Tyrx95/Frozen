package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.database.User;
import com.example.demo.database.UserRepository;



public interface UserService {
	
	 List<User> findAll();
	 void add(User user);
	 void delete(Long id);
	 void update(User user);
	 User getOne(Long id);
	 
	 
	 
	 
}
