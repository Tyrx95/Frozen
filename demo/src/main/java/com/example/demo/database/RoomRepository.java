package com.example.demo.database;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends CrudRepository<Room, Long>{

	public List<Room> findAll();
	public List<Room> findByName(String name);

}
