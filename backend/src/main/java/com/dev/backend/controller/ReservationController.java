package com.dev.backend.controller;

import java.util.List;

import com.dev.backend.model.Book;
import com.dev.backend.model.Client;
import com.dev.backend.model.Reservation;

import com.dev.backend.BookRepository;
import com.dev.backend.ClientRepository;
import com.dev.backend.ReservationRepository;
import com.dev.backend.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Klasa definiująca endpointy dotyczące tabeli z rezerwacjami i ich zachowanie 
 */
@RestController
@RequestMapping("/api")
public class ReservationController {
	/**
	 * Repozytorium zawierające metody dostępu do danych i operacji na tabeli z rezerwacjami 
	 */
	@Autowired
	private ReservationRepository reservationRepository;

	/**
	 * Repozytorium zawierające metody dostępu do danych i operacji na tabeli z książkami 
	 */
    @Autowired
    private BookRepository bookRepository;

	/**
	 * Repozytorium zawierające metody dostępu do danych i operacji na tabeli z klientami 
	 */
    @Autowired
    private ClientRepository clientRepository;

	/**
	 * Endpoint GET zwracający listę wszystkich rezerwacji z bazy danych
	 * @return Lista rezerwacji
	 */
	@GetMapping("/reservations")
	public List<Reservation> getAllReservations() {
		return reservationRepository.findAll();
	}

	/**
	 * Endpoint GET zwracający rezerwację o danym id
	 * @param reservationId Id szukanej rezerwacji
	 * @return Rezerwacja o danym id
	 * @throws ResourceNotFoundException
	 */
	@GetMapping("/reservations/{id}")
	public ResponseEntity<Reservation> getReservationById(@PathVariable(value = "id") int reservationId) throws ResourceNotFoundException {
		Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new ResourceNotFoundException("Reservation not found for this id :: " + reservationId));
		return new ResponseEntity<Reservation>(reservation, HttpStatus.OK);
	}

	/**
	 * Enpoint POST dodający rezerwację do bazy danych
	 * @param reservation Rezerwacja która zostanie dodana do bazy danych
	 * @return Rezerwacja dodana do bazy
	 */
	@PostMapping("/reservations")
	public ResponseEntity<Reservation> createReservation(@RequestBody Reservation reservationRequest) {
		Reservation reservation = new Reservation();

        reservation.setStatus(reservationRequest.getStatus());
        reservation.setDueDate(reservationRequest.getDueDate());
        reservation.setReturnDate(reservationRequest.getReturnDate());

        Book book = bookRepository.findById(reservationRequest.getBookId()).get();
        Client client = clientRepository.findById(reservationRequest.getClientId()).get();

        reservation.setBook(book);
        reservation.setClient(client);

		reservationRepository.save(reservation);

        return new ResponseEntity<Reservation>(reservation, HttpStatus.CREATED);
	}

	/**
	 * Endpoint PUT modyfikujący rezerwację o danym id
	 * @param reservationId Id rezerwacji do modyfikacji
	 * @param reservationRequest Nowe dane rezerwacji
	 * @return Zmodyfikowana rezerwacja
	 * @throws ResourceNotFoundException
	 */
	@PutMapping("/reservations/{id}")
	public ResponseEntity<Reservation> updateReservation(@PathVariable(value = "id") int reservationId, @RequestBody Reservation reservationRequest) throws ResourceNotFoundException {
		Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new ResourceNotFoundException("Reservation not found for this id :: " + reservationId));

        reservation.setStatus(reservationRequest.getStatus());
        reservation.setDueDate(reservationRequest.getDueDate());
        reservation.setReturnDate(reservationRequest.getReturnDate());
        reservation.setBook(reservationRequest.getBook());
        reservation.setClient(reservationRequest.getClient());

		reservationRepository.save(reservation);
        return new ResponseEntity<Reservation>(reservation, HttpStatus.OK);
	}

	/**
	 * Endpoint DELETE usuwający rezerwację o zadanym id
	 * @param reservationId Id rezerwacji która ma zostać usunięta
	 * @throws ResourceNotFoundException
	 */
	@DeleteMapping("/reservations/{id}")
	public ResponseEntity<HttpStatus> deleteReservation(@PathVariable(value = "id") int reservationId) throws ResourceNotFoundException {
		Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new ResourceNotFoundException("Reservation not found for this id :: " + reservationId));

		reservationRepository.delete(reservation);
		return new ResponseEntity<HttpStatus>(HttpStatus.ACCEPTED);
	}
}
