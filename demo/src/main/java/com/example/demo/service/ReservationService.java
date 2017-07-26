package com.example.demo.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.example.demo.database.Reservation;
import com.example.demo.database.User;

public interface ReservationService {

	Iterable<Reservation> getAll();
	void reserve(Reservation reservation);
	void edit(Reservation reservation);
	void delete(Long id);
	Iterable<LocalTime> getFreeRoomCapacitiesOnDate(Long roomId, LocalDate date);
	Reservation getByUser(String viberId);
}
