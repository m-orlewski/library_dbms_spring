package com.dev.backend.postgresql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dev.backend.model.Book;
import com.dev.backend.BookRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class BookController {
	@Autowired
	private BookRepository bookRepository;

	@GetMapping("/books")
	public List<Book> getAllBooks() {
		return bookRepository.findAll();
	}

	@GetMapping("/books/{id}")
	public ResponseEntity<Book> getBookById(@PathVariable(value = "id") Long bookId) {
		Book book = bookRepository.findById(bookId).get();
		return ResponseEntity.ok().body(book);
	}

	@PostMapping("/books")
	public Book createBook(@RequestBody Book book) {
		return bookRepository.save(book);
	}

	@PutMapping("/books/{id}")
	public ResponseEntity<Book> updateBook(@PathVariable(value = "id") Long bookId, @RequestBody Book bookRequest) {
		Book book = bookRepository.findById(bookId).get();

		book.setTitle(bookRequest.getTitle());
		book.setAuthor(bookRequest.getAuthor());
        book.setGenre(bookRequest.getGenre());
		book.setQuantity(bookRequest.getQuantity());
		
		final Book updatedBook = bookRepository.save(book);
		return ResponseEntity.ok(updatedBook);
	}

	@DeleteMapping("/books/{id}")
	public Map<String, Boolean> deleteBook(@PathVariable(value = "id") Long bookId) {
		Book book = bookRepository.findById(bookId).get();

		bookRepository.delete(book);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
