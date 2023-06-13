package com.dev.backend.model;

import jakarta.persistence.*;

/**
 * Klasa reprezentująca klienta w bazie danych
 */
@Entity
@Table(name = "clients")
public class Client {

	/**
	 * Id klienta
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	/**
	 * Imię klienta
	 */
	@Column(name = "first_name", nullable = false)
	private String firstName;
	
	/**
	 * Nazwisko klienta
	 */
	@Column(name = "last_name", nullable = false)
	private String lastName;
	
	public Client() {}
	
	public Client(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public String toString() {
		return "Client [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + "]";
	}
}