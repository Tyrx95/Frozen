package com.example.demo.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.database.Reservation;
import com.example.demo.database.ReservationRepository;
import com.example.demo.database.User;
import com.example.demo.database.UserRepository;

@Service
public class ReservationServiceImpl implements ReservationService{
	@Autowired
	 ReservationRepository repository; 

	public List<Reservation> getAll() {
		List<Reservation> reservations = new ArrayList<>();
		repository.findAll().forEach(reservations::add);
		return reservations;
	}

	@Override
	public void reserve(Reservation reservation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void edit(Reservation reservation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Long id) {
		repository.delete(id);		
	}

	@Override
	public int getFreeRoomCapacitiesOnDate(Long roomId, LocalDate date) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Reservation getByUser(String viberId) {
		// TODO Auto-generated method stub
		return null;
	}
	


}
