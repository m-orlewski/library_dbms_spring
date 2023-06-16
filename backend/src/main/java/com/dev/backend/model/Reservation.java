package com.dev.backend.model;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * Klasa reprezentująca rezerwację w bazie danych
 */
@Entity
@Table(name = "reservations")
public class Reservation {

	/**
	 * Id rezerwacji
	 */
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	/**
	 * Status rezerwacji: 0 - , 1 - 
	 */
	@Column(name = "status", nullable = false)
	private int status;

	/**
	 * Data rezerwacji
	 */
	@Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

	/**
	 * Data zwrotu
	 */
	@Column(name = "return_date", nullable = false)
    private LocalDate returnDate;

	/**
	 * Książka której dotyczy rezerwacja
	 */
    @OneToOne
    @JoinColumn(name = "book_id")
    private Book book;

	/**
	 * Klient którego dotyczy rezerwacja
	 */
    @OneToOne
    @JoinColumn(name = "client_id")
    private Client client;

	public Reservation() {}
	
	public Reservation(int status, LocalDate dueDate, LocalDate returnDate, Book book, Client client) {
        this.status = status;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.book = book;
        this.client = client;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	public LocalDate getDueDate() {
		return dueDate;
	}
	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	public LocalDate getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(LocalDate returnDate) {
		this.returnDate = returnDate;
	}

	public Book getBook() {
		return book;
	}

	public int bookId() {
		return book.getId();
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public Client getClient() {
		return client;
	}

	public int clientId() {
		return client.getId();
	}

	public void setClient(Client client) {
		this.client = client;
	}

	@Override
	public String toString() {
		return "Reservation [id=" + id + ", status=" + status + ", dueDate=" + dueDate + ", returnDate=" + returnDate + "]" +
        "\non " + book + "\nfor " + client;
	}
}