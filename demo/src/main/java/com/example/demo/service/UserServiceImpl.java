package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.database.User;
import com.example.demo.database.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	UserRepository repository;

	public List<User> findAll() {
		List<User> users = new ArrayList<>();
		repository.findAll().forEach(users::add);
		return users;
	}

	@Override
    @Transactional
	public void add(User user) {
		System.out.println("add:"+user);
		repository.save(user);
	}

	public void delete(Long id) {
		repository.delete(id);

	}

	@Override
    @Transactional
	public void subscribe(String viberId) {
		User user = getByViberId(viberId);
		user.setSubscribe(true);
		add(user);
	}

	public void unsubscribe(String viberId) {
		User user = getByViberId(viberId);
		System.out.println("get by ID:"+user);
		user.setSubscribe(false);
		System.out.println("flag changed:"+user);
		add(user);
	}

	public User getByViberId(@RequestParam("viberId") String viberId) {
		return repository.findByViberId(viberId);
	}

}
