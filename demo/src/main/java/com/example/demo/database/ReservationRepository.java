package com.example.demo.database;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends CrudRepository<Reservation, Long>{

	@Query("select time from Reservation where roomId.number = :roomNumber and date = :date")
	public Iterable<LocalTime> getFreeRoomCapacitiesOnDate(@Param("roomNumber") String roomNumber, @Param("date") LocalDate date);
	
	@Query("select r from Reservation r where r.userId.viberId = :viberId)")
	public Iterable<Reservation> getByUser(@Param("viberId") String viberId);

}