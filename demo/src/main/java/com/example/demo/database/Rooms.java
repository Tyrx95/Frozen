package com.example.demo.database;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;

import javax.persistence.Id;

@Entity
@Table(name = "rooms")
public class Rooms implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "number")
	private String number;
	
	@Column(name = "name")
	private String name;
	
	protected Rooms () {}

	public Rooms(String number, String name) {
		super();
		this.number = number;
		this.name = name;
	}

	@Override
	public String toString() {
		return "Rooms [id=" + id + ", number=" + number + ", name=" + name + "]";
	}	
}
