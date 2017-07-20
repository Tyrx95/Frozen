package com.example.demo.database;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;

import org.springframework.data.annotation.Version;

import javax.persistence.Id;

@Entity
@Table(name = "rooms")
public class Room implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Version
	private Integer version;
	
	
	@Column(name = "number")
	private String number;
	
	@Column(name = "name")
	private String name;
	
	protected Room () {}

	public Room( String number, String name) {
		super();
		this.number = number;
		this.name = name;
	}	
	
	@Override
	public String toString() {
		return "Rooms [id=" + id + ", number=" + number + ", name=" + name + "]";
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
	

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	
	
}
