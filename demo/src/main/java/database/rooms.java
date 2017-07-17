package database;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;

import org.springframework.data.annotation.Id;

@Entity
@Table(name = "rooms")
public class rooms implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column(name = "numer")
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
