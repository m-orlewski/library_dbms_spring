package com.dev.backend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dev.backend.model.Book;
import com.dev.backend.model.Client;
import com.dev.backend.ClientRepository;
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
public class ClientController {
	@Autowired
	private ClientRepository clientRepository;

	@GetMapping("/clients")
	public List<Client> getAllClients() {
		return clientRepository.findAll();
	}

	@GetMapping("/clients/{id}")
	public ResponseEntity<Client> getClientById(@PathVariable(value = "id") Long clientId) throws ResourceNotFoundException {
		Client client = clientRepository.findById(clientId).orElseThrow(() -> new ResourceNotFoundException("Client not found for this id :: " + clientId));
		return new ResponseEntity<Client>(client, HttpStatus.OK);
	}

	@PostMapping("/clients")
	public ResponseEntity<Client> createClient(@RequestBody Client client) {
		clientRepository.save(client);
		return new ResponseEntity<Client>(client, HttpStatus.CREATED);
	}

	@PutMapping("/clients/{id}")
	public ResponseEntity<Client> updateClient(@PathVariable(value = "id") Long clientId, @RequestBody Client clientRequest) throws ResourceNotFoundException {
		Client client = clientRepository.findById(clientId).orElseThrow(() -> new ResourceNotFoundException("Client not found for this id :: " + clientId));

		client.setFirstName(clientRequest.getFirstName());
		client.setLastName(clientRequest.getLastName());
		
		clientRepository.save(client);
		return new ResponseEntity<Client>(client, HttpStatus.OK);
	}

	@DeleteMapping("/clients/{id}")
	public ResponseEntity<HttpStatus> deleteClient(@PathVariable(value = "id") Long clientId) throws ResourceNotFoundException {
		Client client = clientRepository.findById(clientId).orElseThrow(() -> new ResourceNotFoundException("Client not found for this id :: " + clientId));

		clientRepository.delete(client);
		return new ResponseEntity<HttpStatus>(HttpStatus.ACCEPTED);
	}
}
