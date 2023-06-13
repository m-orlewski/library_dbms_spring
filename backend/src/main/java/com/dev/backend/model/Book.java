package com.dev.backend.model;

import jakarta.persistence.*;

/**
 * Klasa reprezentująca książkę w bazie danych
 */
@Entity
@Table(name = "books")
public class Book {

	/**
	 * Id książki
	 */
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	/**
	 * Tytuł książki
	 */
	@Column(name = "title", nullable = false)
	private String title;

	/**
	 * Imię i nazwisko autora książki
	 */
	@Column(name = "author", nullable = false)
    private String author;

	/**
	 * Nazwa gatunku książki
	 */
	@Column(name = "genre", nullable = false)
    private String genre;

	/**
	 * Ilość egzemplarzy
	 */
	@Column(name = "quantity", nullable = false)
    private int quantity;
	
	
	public Book() {}
	
	public Book(String title, String author, String genre, int quantity) {
		this.title = title;
        this.author = author;
        this.genre = genre;
        this.quantity = quantity;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}

	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "Book [id=" + id + ", author=" + author + ", genre=" + genre + ", quantity=" + quantity + "]";
	}
}