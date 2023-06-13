package com.dev.backend.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "status", nullable = false)
	private int status;

	@Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

	@Column(name = "return_date", nullable = false)
    private LocalDate returnDate;

    @OneToOne
    @JoinColumn(name = "book_id")
    private Book book;

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

	public int getBookId() {
		return book.getId();
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public Client getClient() {
		return client;
	}

	public int getClientId() {
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