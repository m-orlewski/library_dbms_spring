package com.dev.backend;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
import com.fasterxml.jackson.datatype.*;
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

    Book book1 = new Book("book1", "author1", "genre1", 1);
    Book book2 = new Book("book2", "author2", "genre2", 2);

    Client client1 = new Client("fname1", "lname1");
    Client client2 = new Client("fname1", "lname2");

    Reservation reservation1 = new Reservation(0, LocalDate.of(2023, 6, 10), LocalDate.of(2023, 6, 15), book1, client1);
    Reservation reservation2 = new Reservation(1, LocalDate.of(2023, 6, 15), LocalDate.of(2023, 6, 22), book1, client2);
    Reservation reservation3 = new Reservation(0, LocalDate.of(2023, 6, 11), LocalDate.of(2023, 6, 25), book2, client2);

    @Test
    public void getAllReservations_success() throws Exception {
        List<Reservation> reservations = new ArrayList<>(Arrays.asList(reservation1, reservation2, reservation3));
        
        Mockito.when(reservationRepository.findAll()).thenReturn(reservations);
        
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(reservation1.getId())))
                .andExpect(jsonPath("$[1].id", is(reservation2.getId())))
                .andExpect(jsonPath("$[2].id", is(reservation3.getId())));
    }

    @Test
    public void getReservationById_success() throws Exception {      
        Mockito.when(reservationRepository.findById(reservation1.getId())).thenReturn(Optional.of(reservation1));
        
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/reservations/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.id", is(reservation1.getId())));
    }

    @Test
    public void getClientById_notFound() throws Exception {      
        String exceptionParam = "not_found";

        mockMvc.perform(MockMvcRequestBuilders
        .get("/api/reservations/1", exceptionParam)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
        .andExpect(result -> assertEquals("Reservation not found for this id :: 1", result.getResolvedException().getMessage()));
    }

    @Test
    public void createReservation_success() throws Exception {
        mapper.registerModule(new JavaTimeModule());
        Mockito.when(reservationRepository.save(reservation1)).thenReturn(reservation1);
        Mockito.when(bookRepository.findById(book1.getId())).thenReturn(Optional.of(book1));
        Mockito.when(clientRepository.findById(client1.getId())).thenReturn(Optional.of(client1));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/reservations")
                .content(asJsonString(reservation1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(reservation1.getId())));
    }

    @Test
    public void updateReservation_success() throws Exception {

        Reservation updatedReservation = new Reservation(0, LocalDate.of(2023, 6, 10), LocalDate.of(2024, 6, 15), book1, client1);

        Mockito.when(reservationRepository.findById(reservation1.getId())).thenReturn(Optional.of(reservation1));
        Mockito.when(reservationRepository.save(updatedReservation)).thenReturn(updatedReservation);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/api/reservations/{id}", 0)
                .content(asJsonString(updatedReservation))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(updatedReservation.getId())));
    }

    @Test
    public void deleteReservation_success() throws Exception {

        Mockito.when(reservationRepository.findById(reservation1.getId())).thenReturn(Optional.of(reservation1));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/reservations/{id}", 0))
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
