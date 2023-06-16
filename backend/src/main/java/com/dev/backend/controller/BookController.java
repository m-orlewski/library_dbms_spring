package com.dev.backend.controller;

import java.util.List;

import com.dev.backend.model.Book;
import com.dev.backend.BookRepository;
import com.dev.backend.annotation.LogExecutionTime;
import com.dev.backend.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Klasa definiująca endpointy dotyczące tabeli z książkami i ich zachowanie 
 */
@CrossOrigin
@RestController
@RequestMapping("/api")
public class BookController {
	/**
	 * Repozytorium zawierające metody dostępu do danych i operacji na tabeli z książkami 
	 */
	@Autowired
	private BookRepository bookRepository;

	/**
	 * Endpoint GET zwracający listę wszystkich książek z bazy danych
	 * @return Lista książek
	 */
	@RequestMapping(method = RequestMethod.GET, path = "/books")
	@LogExecutionTime
	public ResponseEntity<List<Book>> getAllBooks() {
		List<Book> books = bookRepository.findAll();
		return new ResponseEntity<List<Book>>(books, HttpStatus.OK);
	}

	/**
	 * Endpoint GET zwracający książkę o danym id
	 * @param bookId Id szukanej książki
	 * @return Książka o danym id
	 * @throws ResourceNotFoundException
	 */
	@RequestMapping(method = RequestMethod.GET, path = "/books/{id}")
	@LogExecutionTime
	public ResponseEntity<Book> getBookById(@PathVariable(value = "id") int bookId) throws ResourceNotFoundException {
		Book book = bookRepository.findById(bookId).orElseThrow(() -> new ResourceNotFoundException("Book not found for this id :: " + bookId));
		return new ResponseEntity<Book>(book, HttpStatus.OK);
	}

	/**
	 * Enpoint POST dodający książkę do bazy danych
	 * @param book Książka która zostanie dodana do bazy danych
	 * @return Książka dodana do bazy
	 */
	@RequestMapping(method = RequestMethod.POST, path = "/books")
	@LogExecutionTime
	public ResponseEntity<Book> createBook(@RequestBody Book book) {
		bookRepository.save(book);
		return new ResponseEntity<Book>(book, HttpStatus.CREATED);
	}

	/**
	 * Endpoint PUT modyfikujący książkę o danym id
	 * @param bookId Id książki do modyfikacji
	 * @param bookRequest Nowe dane książki
	 * @return Zmodyfikowana książka
	 * @throws ResourceNotFoundException
	 */
	@RequestMapping(method = RequestMethod.PUT, path = "/books/{id}")
	@LogExecutionTime
	public ResponseEntity<Book> updateBook(@PathVariable(value = "id") int bookId, @RequestBody Book bookRequest) throws ResourceNotFoundException {
		Book book = bookRepository.findById(bookId).orElseThrow(() -> new ResourceNotFoundException("Book not found for this id :: " + bookId));

		book.setTitle(bookRequest.getTitle());
		book.setAuthor(bookRequest.getAuthor());
        book.setGenre(bookRequest.getGenre());
		book.setQuantity(bookRequest.getQuantity());
		
		bookRepository.save(book);
		return new ResponseEntity<Book>(book, HttpStatus.OK);
	}

	/**
	 * Endpoint DELETE usuwający książkę o zadanym id
	 * @param bookId Id książki która ma zostać usunięta
	 * @throws ResourceNotFoundException
	 */
	@RequestMapping(method = RequestMethod.DELETE, path = "/books/{id}")
	@LogExecutionTime
	public ResponseEntity<HttpStatus> deleteBook(@PathVariable(value = "id") int bookId) throws ResourceNotFoundException {
		Book book = bookRepository.findById(bookId).orElseThrow(() -> new ResourceNotFoundException("Book not found for this id :: " + bookId));

		bookRepository.delete(book);
		return new ResponseEntity<HttpStatus>(HttpStatus.ACCEPTED);
	}
}
