package com.dev.backend.controller;

import java.util.List;

import com.dev.backend.model.Book;
import com.dev.backend.model.Client;
import com.dev.backend.model.Reservation;

import com.dev.backend.BookRepository;
import com.dev.backend.ClientRepository;
import com.dev.backend.ReservationRepository;
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
 * Klasa definiująca endpointy dotyczące tabeli z rezerwacjami i ich zachowanie 
 */
@RestController
@CrossOrigin
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
	@RequestMapping(method = RequestMethod.GET, path = "/reservations")
	@LogExecutionTime
	public ResponseEntity<List<Reservation>> getAllReservations() {
		List<Reservation> reservations = reservationRepository.findAll();
		return new ResponseEntity<List<Reservation>>(reservations, HttpStatus.OK);
	}

	/**
	 * Endpoint GET zwracający rezerwację o danym id
	 * @param reservationId Id szukanej rezerwacji
	 * @return Rezerwacja o danym id
	 * @throws ResourceNotFoundException
	 */
	@RequestMapping(method = RequestMethod.GET, path = "/reservations/{id}")
	@LogExecutionTime
	public ResponseEntity<Reservation> getReservationById(@PathVariable(value = "id") int reservationId) throws ResourceNotFoundException {
		Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new ResourceNotFoundException("Reservation not found for this id :: " + reservationId));
		return new ResponseEntity<Reservation>(reservation, HttpStatus.OK);
	}

	/**
	 * Enpoint POST dodający rezerwację do bazy danych
	 * @param reservation Rezerwacja która zostanie dodana do bazy danych
	 * @return Obiekt klasy ErrorResponse z opcjonalnym błędem
	 */
	@RequestMapping(method = RequestMethod.POST, path = "/reservations")
	@LogExecutionTime
	public ResponseEntity<String> createReservation(@RequestBody Reservation reservationRequest) {
		Reservation reservation = new Reservation();

        reservation.setDueDate(reservationRequest.getDueDate());
        reservation.setReturnDate(reservationRequest.getReturnDate());

        Book book = bookRepository.findById(reservationRequest.bookId()).get();
        Client client = clientRepository.findById(reservationRequest.clientId()).get();

        reservation.setBook(book);
        reservation.setClient(client);

		if (isReservationValid(reservation)) {
			reservationRepository.save(reservation);
			return ResponseEntity.status(HttpStatus.CREATED).build();
		}
		else {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("The reservation dates overlap with existing reservations");
		}
	}

	/**
	 * Endpoint PUT modyfikujący rezerwację o danym id
	 * @param reservationId Id rezerwacji do modyfikacji
	 * @param reservationRequest Nowe dane rezerwacji
	 * @return Obiekt klasy ErrorResponse z opcjonalnym błędem
	 * @throws ResourceNotFoundException
	 */
	@RequestMapping(method = RequestMethod.PUT, path = "/reservations/{id}")
	@LogExecutionTime
	public ResponseEntity<String> updateReservation(@PathVariable(value = "id") int reservationId, @RequestBody Reservation reservationRequest) throws ResourceNotFoundException {
		Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new ResourceNotFoundException("Reservation not found for this id :: " + reservationId));

        reservation.setDueDate(reservationRequest.getDueDate());
        reservation.setReturnDate(reservationRequest.getReturnDate());
        reservation.setBook(reservationRequest.getBook());
        reservation.setClient(reservationRequest.getClient());

		if (isReservationValid(reservation)) {
			reservationRepository.save(reservation);
			return ResponseEntity.status(HttpStatus.OK).build();
		}
		else {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("The reservation dates overlap with existing reservations");
		}
	}

	/**
	 * Endpoint DELETE usuwający rezerwację o zadanym id
	 * @param reservationId Id rezerwacji która ma zostać usunięta
	 * @throws ResourceNotFoundException
	 */
	@RequestMapping(method = RequestMethod.DELETE, path = "/reservations/{id}")
	@LogExecutionTime
	public ResponseEntity<HttpStatus> deleteReservation(@PathVariable(value = "id") int reservationId) throws ResourceNotFoundException {
		Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new ResourceNotFoundException("Reservation not found for this id :: " + reservationId));

		reservationRepository.delete(reservation);
		return new ResponseEntity<HttpStatus>(HttpStatus.ACCEPTED);
	}

	public boolean isReservationValid(Reservation reservation) {
		// lista rezerwacji na daną ksiażkę z wykluczeniem rezerwacji którą dodajemy/edytujemy
		System.out.println("***** isReservationValid for " + reservation);
		List<Reservation> currentReservations = reservationRepository.findByBookAndIdNot(reservation.getBook(), reservation.getId());

		if (currentReservations.size() < reservation.getBook().getQuantity()) {
			System.out.println("***** Reservation is valid because " + currentReservations.size() + " < " + reservation.getBook().getQuantity());
			return true; // dostępny egzemplarz bez rezerwacji
		}

		// rezerwacji jest więcej niż egzemplarzy - sprawdzić czy daty na siebie nie nachodzą
		boolean datesOverlap = true;
		for (Reservation currentReservation : currentReservations) {
			if (reservation.getReturnDate().isBefore(currentReservation.getDueDate()) ||
				reservation.getDueDate().isAfter(currentReservation.getReturnDate())) {
					datesOverlap = false;
					break;
				}
		}

		if (datesOverlap) {
			System.out.println("***** Reservation is NOT valid because datesOverlap = true");
			return false; // nowa rezerwacja koliduje z obecnymi
		}

		System.out.println("***** Reservation is valid because datesOverlap = false");
		return true; // nowa rezerwacja nie koliduje z obecnymi
	}
}
