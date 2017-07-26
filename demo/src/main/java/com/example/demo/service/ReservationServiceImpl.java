package com.example.demo.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

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
	@Transactional
	public void reserve(Reservation reservation) {
		repository.save(reservation);	
		
	}

	@Override
	@Transactional
	public void edit(Reservation reservation) {
		repository.save(reservation);		
	}

	@Override
	@Transactional
	public void delete(Long id) {
	      repository.delete(id);		
	}

	@Override
	public Iterable<LocalTime> getFreeRoomCapacitiesOnDate(String roomNumber, LocalDate date) {
		return repository.getFreeRoomCapacitiesOnDate(roomNumber, date);
	}

	@Override
	public Reservation getByUser(String viberId) {
		return repository.getByUser(viberId);
	}
	


}
