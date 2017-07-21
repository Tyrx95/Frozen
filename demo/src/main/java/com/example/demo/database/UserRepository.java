package com.example.demo.database;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends CrudRepository<User, Long>{
	public List<User> findAll();
	public List<User> findByName(String name);
}
