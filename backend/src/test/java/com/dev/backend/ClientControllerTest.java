package com.dev.backend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.dev.backend.controller.ClientController;
import com.dev.backend.exception.ResourceNotFoundException;
import com.dev.backend.model.Client;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ClientController.class)
public class ClientControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;
    
    @MockBean
    ClientRepository clientRepository;

    Client client1 = new Client("fname1", "lname1");
    Client client2 = new Client("fname1", "lname2");
    Client client3 = new Client("fname1", "lname3");

    @Test
    public void getAllClients_success() throws Exception {
        List<Client> clients = new ArrayList<>(Arrays.asList(client1, client2, client3));
        
        Mockito.when(clientRepository.findAll()).thenReturn(clients);
        
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/clients")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].firstName", is(client1.getFirstName())))
                .andExpect(jsonPath("$[1].firstName", is(client2.getFirstName())))
                .andExpect(jsonPath("$[2].firstName", is(client3.getFirstName())));
    }

    @Test
    public void getClientById_success() throws Exception {      
        Mockito.when(clientRepository.findById(client1.getId())).thenReturn(Optional.of(client1));
        
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/clients/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.firstName", is(client1.getFirstName())));
    }

    @Test
    public void getClientById_notFound() throws Exception {      
        String exceptionParam = "not_found";

        mockMvc.perform(MockMvcRequestBuilders
        .get("/api/clients/1", exceptionParam)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
        .andExpect(result -> assertEquals("Client not found for this id :: 1", result.getResolvedException().getMessage()));
    }

    @Test
    public void createClient_success() throws Exception {
        Mockito.when(clientRepository.save(client1)).thenReturn(client1);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/clients")
                .content(asJsonString(client1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(client1.getFirstName())));
    }

    @Test
    public void updateClient_success() throws Exception {

        Client updatedClient = new Client("updatedFName1", "updatedLName1");

        Mockito.when(clientRepository.findById(client1.getId())).thenReturn(Optional.of(client1));
        Mockito.when(clientRepository.save(updatedClient)).thenReturn(updatedClient);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/api/clients/{id}", 0)
                .content(asJsonString(updatedClient))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(updatedClient.getFirstName())));
    }

    @Test
    public void deleteClient_success() throws Exception {

        Mockito.when(clientRepository.findById(client1.getId())).thenReturn(Optional.of(client1));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/clients/{id}", 0))
                    .andExpect(status().isAccepted());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
