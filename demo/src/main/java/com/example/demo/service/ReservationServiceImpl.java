package com.example.demo.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.database.Reservation;
import com.example.demo.database.ReservationRepository;
import com.example.demo.database.User;

@Service
public class ReservationServiceImpl {
	@Autowired
	 ReservationRepository repository; 
	
	void reserve(Reservation reservation){
		//ToDo
	};
	void edit(Reservation reservation){
		//ToDo
	};
	void delete(Long id){
		//ToDo
	};

}
