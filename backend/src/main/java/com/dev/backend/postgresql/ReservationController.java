package com.dev.backend.postgresql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dev.backend.model.Book;
import com.dev.backend.model.Client;
import com.dev.backend.model.Reservation;
import com.dev.backend.BookRepository;
import com.dev.backend.ClientRepository;
import com.dev.backend.ReservationRepository;
import com.dev.backend.exception.ResourceNotFoundException;

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
	public ResponseEntity<Reservation> getReservationById(@PathVariable(value = "id") Long reservationId) throws ResourceNotFoundException {
		Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new ResourceNotFoundException("Reservation not found for this id :: " + reservationId));
		return ResponseEntity.ok().body(reservation);
	}

	@PostMapping("/reservations")
	public Reservation createReservation(@RequestBody Reservation reservationRequest) {
		Reservation reservation = new Reservation();

        reservation.setStatus(reservationRequest.getStatus());
        reservation.setDueDate(reservationRequest.getDueDate());
        reservation.setReturnDate(reservationRequest.getReturnDate());

        Book book = bookRepository.findById(reservationRequest.getBookId()).get();
        Client client = clientRepository.findById(reservationRequest.getClientId()).get();

        reservation.setBook(book);
        reservation.setClient(client);

        return reservationRepository.save(reservation);
	}

	@PutMapping("/reservations/{id}")
	public ResponseEntity<Reservation> updateReservation(@PathVariable(value = "id") Long reservationId, @RequestBody Reservation reservationRequest) throws ResourceNotFoundException {
		Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new ResourceNotFoundException("Reservation not found for this id :: " + reservationId));

        reservation.setStatus(reservationRequest.getStatus());
        reservation.setDueDate(reservationRequest.getDueDate());
        reservation.setReturnDate(reservationRequest.getReturnDate());
        reservation.setBook(reservationRequest.getBook());
        reservation.setClient(reservationRequest.getClient());

		final Reservation updatedReservation = reservationRepository.save(reservation);
        return ResponseEntity.ok().body(updatedReservation);
	}

	@DeleteMapping("/reservations/{id}")
	public Map<String, Boolean> deleteReservation(@PathVariable(value = "id") Long reservationId) throws ResourceNotFoundException {
		Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new ResourceNotFoundException("Reservation not found for this id :: " + reservationId));

		reservationRepository.delete(reservation);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
