package com.example.demo.database;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;

import org.springframework.data.annotation.Version;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Id;

@Entity
@Table(name = "reservations")
public class Reservation implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "userId")
	private Long userId;
	
	@Column(name = "roomId")
	private Long roomId;
	
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	@Column (name = "date")
	private LocalDate date;
	
	@DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
	@Column (name = "time")
	private LocalTime time;
	
	public Reservation(long userId, long roomId, LocalDate date, LocalTime time) {
		super();
		this.userId = userId;
		this.roomId = roomId;
		this.date = date;
		this.time = time;
	}
	

	@Override
	public String toString() {
		return "Reservations [id=" + id + ", user=" + userId + ", room=" + roomId + ", date=" + date + ", time=" + time + "]";
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Long getUserId() {
		return userId;
	}


	public void setUserId(Long userId) {
		this.userId = userId;
	}


	public Long getRoomId() {
		return roomId;
	}


	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}


	public LocalDate getDate() {
		return date;
	}


	public void setDate(LocalDate date) {
		this.date = date;
	}


	public LocalTime getTime() {
		return time;
	}


	public void setTime(LocalTime time) {
		this.time = time;
	}


	
	
}