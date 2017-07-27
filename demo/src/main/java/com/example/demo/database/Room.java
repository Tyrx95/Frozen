package com.example.demo.database;

import java.io.Serializable;
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
@Table(name = "rooms")
public class Room implements Serializable {

	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "number", unique=true)
	private String number;
	
	@Column(name = "name")
	private String name;
	
	@DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
	@Column (name = "startWorkTime")
	private LocalTime startWorkTime;
	
	@DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
	@Column (name = "endWorkTime")
	private LocalTime endWorkTime;
	
	
	
	
	

	public Room( String number, String name, LocalTime startWorkTime, LocalTime endWorkTime) {
		super();
		this.number = number;
		this.name = name;
		this.startWorkTime = startWorkTime;
		this.endWorkTime = endWorkTime;
	}

	public Room () {}

	@Override
	public String toString() {
		return "Rooms [number=" + number + ", name=" + name + "]";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	} 
	

	public LocalTime getStartWorkTime() {
		return startWorkTime;
	}

	public void setStartWorkTime(LocalTime startWorkTime) {
		this.startWorkTime = startWorkTime;
	}

	public LocalTime getEndWorkTime() {
		return endWorkTime;
	}

	public void setEndWorkTime(LocalTime endWorkTime) {
		this.endWorkTime = endWorkTime;
	}
	
	
}
