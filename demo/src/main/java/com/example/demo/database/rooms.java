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
public class rooms implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "number")
	private String number;
	
	@Column(name = "name")
	private String name;
	
	protected rooms () {}

	public rooms(Integer id, String number, String name) {
		super();
		this.id = id;
		this.number = number;
		this.name = name;
	}	
}
