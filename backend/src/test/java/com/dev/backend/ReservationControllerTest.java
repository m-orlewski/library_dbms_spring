package com.dev.backend;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.dev.backend.controller.ReservationController;
import com.dev.backend.exception.ResourceNotFoundException;
import com.dev.backend.model.Book;
import com.dev.backend.model.Client;
import com.dev.backend.model.Reservation;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@WebMvcTest(ReservationController.class)
public class ReservationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;
    
    @MockBean
    ReservationRepository reservationRepository;

    @MockBean
    BookRepository bookRepository;

    @MockBean
    ClientRepository clientRepository;

    ReservationController reservationController;

    private Book book1;
    private Book book2;

    private Client client1;
    private Client client2;

    private Reservation reservation1;
    private Reservation reservation2;
    private Reservation reservation3;
    private Reservation reservation4;

    @BeforeEach
    public void setUp() {

        reservationController = new ReservationController();

        book1 = new Book("book1", "author1", "genre1", 2);
        book2 = new Book("book2", "author2", "genre2", 2);

        client1 = new Client("fname1", "lname1");
        client2 = new Client("fname1", "lname2");

        book1.setId(1);
        book2.setId(2);

        client1.setId(1);
        client2.setId(2);

        reservation1 = new Reservation(LocalDate.of(2023, 6, 10), LocalDate.of(2023, 6, 15), book1, client1);
        reservation2 = new Reservation(LocalDate.of(2023, 6, 9), LocalDate.of(2023, 6, 13), book1, client2);
        reservation3 = new Reservation(LocalDate.of(2023, 6, 1), LocalDate.of(2023, 6, 25), book1, client2);
        reservation4 = new Reservation(LocalDate.of(2023, 6, 11), LocalDate.of(2023, 6, 25), book2, client2);

        reservation1.setId(1);
        reservation2.setId(2);
        reservation3.setId(3);
        reservation4.setId(4);
    }

    @Test
    public void getAllReservations_success() throws Exception {
        List<Reservation> reservations = new ArrayList<>(Arrays.asList(reservation1, reservation2, reservation3, reservation4));
        
        Mockito.when(reservationRepository.findAll()).thenReturn(reservations);
        
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].id", is(reservation1.getId())))
                .andExpect(jsonPath("$[1].id", is(reservation2.getId())))
                .andExpect(jsonPath("$[2].id", is(reservation3.getId())))
                .andExpect(jsonPath("$[3].id", is(reservation4.getId())));
    }

    @Test
    public void getReservationById_success() throws Exception {      
        Mockito.when(reservationRepository.findById(reservation1.getId())).thenReturn(Optional.of(reservation1));
        
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/reservations/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.id", is(reservation1.getId())));
    }

    @Test
    public void getReservationById_notFound() throws Exception {      
        String exceptionParam = "not_found";

        mockMvc.perform(MockMvcRequestBuilders
        .get("/api/reservations/1", exceptionParam)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
        .andExpect(result -> assertEquals("Reservation not found for this id :: 1", result.getResolvedException().getMessage()));
    }

    @Test
    public void createReservationFirstReservation_success() throws Exception {
        mapper.registerModule(new JavaTimeModule());

        Mockito.when(reservationRepository.save(reservation1)).thenReturn(reservation1);
        Mockito.when(bookRepository.findById(book1.getId())).thenReturn(Optional.of(book1));
        Mockito.when(clientRepository.findById(client1.getId())).thenReturn(Optional.of(client1));
        Mockito.when(reservationRepository.findByBookAndIdNot(book1, reservation1.getId())).thenReturn(Collections.emptyList());

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/reservations")
                .content(asJsonString(reservation1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated());
    }

    @Test
    public void createReservationDatesOverlapButBelowQuantity_success() throws Exception {
        mapper.registerModule(new JavaTimeModule());

        List<Reservation> currentReservations = new ArrayList<>(Arrays.asList(reservation2));

        Mockito.when(reservationRepository.save(reservation1)).thenReturn(reservation1);
        Mockito.when(bookRepository.findById(book1.getId())).thenReturn(Optional.of(book1));
        Mockito.when(clientRepository.findById(client1.getId())).thenReturn(Optional.of(client1));
        Mockito.when(reservationRepository.findByBookAndIdNot(book1, 0)).thenReturn(currentReservations);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/reservations")
                .content(asJsonString(reservation1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated());
    }

    @Test
    public void createReservationOverQuantityButDatesDoNotOverlap_success() throws Exception {
        mapper.registerModule(new JavaTimeModule());

        Reservation noOverlapReservation = new Reservation(LocalDate.of(2023, 6, 8), LocalDate.of(2023, 6, 9), book1, client1);

        List<Reservation> currentReservations = new ArrayList<>(Arrays.asList(reservation2, noOverlapReservation));

        Mockito.when(reservationRepository.save(reservation1)).thenReturn(reservation1);
        Mockito.when(bookRepository.findById(book1.getId())).thenReturn(Optional.of(book1));
        Mockito.when(clientRepository.findById(client1.getId())).thenReturn(Optional.of(client1));
        Mockito.when(reservationRepository.findByBookAndIdNot(book1, 0)).thenReturn(currentReservations);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/reservations")
                .content(asJsonString(reservation1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated());
    }

    @Test
    public void createReservationOverQuantityDatesOverlap_failure() throws Exception {
        mapper.registerModule(new JavaTimeModule());

        Reservation overlapReservation = new Reservation(LocalDate.of(2023, 6, 12), LocalDate.of(2023, 6, 17), book1, client1);

        List<Reservation> currentReservations = new ArrayList<>(Arrays.asList(reservation2, overlapReservation));

        Mockito.when(reservationRepository.save(reservation1)).thenReturn(reservation1);
        Mockito.when(bookRepository.findById(book1.getId())).thenReturn(Optional.of(book1));
        Mockito.when(clientRepository.findById(client1.getId())).thenReturn(Optional.of(client1));
        Mockito.when(reservationRepository.findByBookAndIdNot(book1, 0)).thenReturn(currentReservations);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/reservations")
                .content(asJsonString(reservation1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(mockRequest)
                .andExpect(status().isConflict());
    }

    @Test
    public void updateReservationFirstReservation_success() throws Exception {

        Reservation updatedReservation = new Reservation(LocalDate.of(2023, 6, 10), LocalDate.of(2024, 6, 15), book1, client1);

        Mockito.when(reservationRepository.findById(reservation1.getId())).thenReturn(Optional.of(reservation1));
        Mockito.when(reservationRepository.save(updatedReservation)).thenReturn(updatedReservation);
        Mockito.when(reservationRepository.findByBookAndIdNot(book1, reservation1.getId())).thenReturn(Collections.emptyList());

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/api/reservations/{id}", 1)
                .content(asJsonString(updatedReservation))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void deleteReservation_success() throws Exception {

        Mockito.when(reservationRepository.findById(reservation1.getId())).thenReturn(Optional.of(reservation1));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/reservations/{id}", 1))
                    .andExpect(status().isAccepted());
    }

    public String asJsonString(final Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
