package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.database.User;
import com.example.demo.database.UserRepository;

@Service
public class UserServiceImpl {
	@Autowired
	 UserRepository repository; 
	 
	 public List<User> findAll() {
		    List<User> users = new ArrayList<>();
		    repository.findAll().forEach(users::add);
		    return users;
		}
	 
	 public void add(User user){
		 
		 repository.save(user);
	 }

	public void delete(Long id) {
		// TODO Auto-generated method stub
		repository.delete(id);
		
	}

	public void update(User user) {
		repository.save(user);
		
	}

	public User getOne(Long id) {
		return repository.findOne(id);
	}
}
