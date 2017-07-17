package com.example.demo.database;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface repository extends  CrudRepository<rooms, Long>{


}
