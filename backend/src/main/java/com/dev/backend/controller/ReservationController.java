package com.dev.backend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dev.backend.model.Book;
import com.dev.backend.model.Client;
import com.dev.backend.model.Reservation;

import io.micrometer.core.ipc.http.HttpSender.Response;

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

@RestController
@RequestMapping("/api")
public class ReservationController {
	@Autowired
	private ReservationRepository reservationRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ClientRepository clientRepository;

	@GetMapping("/reservations")
	public List<Reservation> getAllReservations() {
		return reservationRepository.findAll();
	}

	@GetMapping("/reservations/{id}")
	public ResponseEntity<Reservation> getReservationById(@PathVariable(value = "id") int reservationId) throws ResourceNotFoundException {
		Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new ResourceNotFoundException("Reservation not found for this id :: " + reservationId));
		return new ResponseEntity<Reservation>(reservation, HttpStatus.OK);
	}

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

	@DeleteMapping("/reservations/{id}")
	public ResponseEntity<HttpStatus> deleteReservation(@PathVariable(value = "id") int reservationId) throws ResourceNotFoundException {
		Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new ResourceNotFoundException("Reservation not found for this id :: " + reservationId));

		reservationRepository.delete(reservation);
		return new ResponseEntity<HttpStatus>(HttpStatus.ACCEPTED);
	}
}
