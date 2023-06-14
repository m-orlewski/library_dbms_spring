package com.dev.backend.controller;

import java.util.List;

import com.dev.backend.model.Book;
import com.dev.backend.model.Client;
import com.dev.backend.ClientRepository;
import com.dev.backend.annotation.LogExecutionTime;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Klasa definiująca endpointy dotyczące tabeli z książkami i ich zachowanie
 */
@RestController
@RequestMapping("/api")
public class ClientController {
	/**
	 * Repozytorium zawierające metody dostępu do danych i operacji na tabeli z klientami 
	 */
	@Autowired
	private ClientRepository clientRepository;

	/**
	 * Endpoint GET zwracający listę wszystkich klientów z bazy danych
	 * @return Lista klientów
	 */
	@RequestMapping(method = RequestMethod.GET, path = "/clients")
	@LogExecutionTime
	public ResponseEntity<List<Client>> getAllClients() {
		List<Client> clients = clientRepository.findAll();
		return new ResponseEntity<List<Client>>(clients, HttpStatus.OK);
	}

	/**
	 * Endpoint GET zwracający klienta o danym id
	 * @param clientId Id szukanego klienta
	 * @return Klient o danym id
	 * @throws ResourceNotFoundException
	 */
	@RequestMapping(method = RequestMethod.GET, path = "/clients/{id}")
	@LogExecutionTime
	public ResponseEntity<Client> getClientById(@PathVariable(value = "id") int clientId) throws ResourceNotFoundException {
		Client client = clientRepository.findById(clientId).orElseThrow(() -> new ResourceNotFoundException("Client not found for this id :: " + clientId));
		return new ResponseEntity<Client>(client, HttpStatus.OK);
	}

	/**
	 * Enpoint POST dodający klienta do bazy danych
	 * @param client Klient który zostanie dodany do bazy danych
	 * @return Klient dodany do bazy
	 */
	@RequestMapping(method = RequestMethod.POST, path = "/clients")
	@LogExecutionTime
	public ResponseEntity<Client> createClient(@RequestBody Client client) {
		clientRepository.save(client);
		return new ResponseEntity<Client>(client, HttpStatus.CREATED);
	}

	/**
	 * Endpoint PUT modyfikujący klienta o danym id
	 * @param clientId Id klienta do modyfikacji
	 * @param clientRequest Nowe dane klienta
	 * @return Zmodyfikowanu klient
	 * @throws ResourceNotFoundException
	 */
	@RequestMapping(method = RequestMethod.PUT, path = "/clients/{id}")
	@LogExecutionTime
	public ResponseEntity<Client> updateClient(@PathVariable(value = "id") int clientId, @RequestBody Client clientRequest) throws ResourceNotFoundException {
		Client client = clientRepository.findById(clientId).orElseThrow(() -> new ResourceNotFoundException("Client not found for this id :: " + clientId));

		client.setFirstName(clientRequest.getFirstName());
		client.setLastName(clientRequest.getLastName());
		
		clientRepository.save(client);
		return new ResponseEntity<Client>(client, HttpStatus.OK);
	}

	/**
	 * Endpoint DELETE usuwający klienta o zadanym id
	 * @param clientId Id klienta który ma zostać usunięty
	 * @throws ResourceNotFoundException
	 */
	@RequestMapping(method = RequestMethod.DELETE, path = "/clients/{id}")
	@LogExecutionTime
	public ResponseEntity<HttpStatus> deleteClient(@PathVariable(value = "id") int clientId) throws ResourceNotFoundException {
		Client client = clientRepository.findById(clientId).orElseThrow(() -> new ResourceNotFoundException("Client not found for this id :: " + clientId));

		clientRepository.delete(client);
		return new ResponseEntity<HttpStatus>(HttpStatus.ACCEPTED);
	}
}
