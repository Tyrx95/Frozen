package com.example.demo.database;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends  CrudRepository<Rooms, Long>{
	List<Rooms> findByName(String name);

}
