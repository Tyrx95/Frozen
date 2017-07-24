package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;

import com.example.demo.database.Reservation;
import com.example.demo.database.User;

public interface ReservationService {

	List<Reservation>getAll();
	void reserve(Reservation reservation);
	void edit(Reservation reservation);
	void delete(Long id);
	int getFreeRoomCapacitiesOnDate(Long roomId, LocalDate date);
	Reservation getByUser(String viberId);
}
