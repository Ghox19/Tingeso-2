package com.microservice.clientMicroservice.Controllers;

import com.microservice.clientMicroservice.DTOS.ClientGetForm;
import com.microservice.clientMicroservice.DTOS.ClientInfoRequiredForm;
import com.microservice.clientMicroservice.DTOS.DocumentSafeForm;
import com.microservice.clientMicroservice.DTOS.RegisterForm;
import com.microservice.clientMicroservice.Entities.ClientEntity;
import com.microservice.clientMicroservice.Services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(path = "/client")
public class ClientController {
    @Autowired
    private ClientService clientService;

    //Function to add a user
    @PostMapping
    public ResponseEntity<Object> addClient(@RequestBody RegisterForm client) {
        return this.clientService.addClient(client);
    }

    @GetMapping
    public List<ClientGetForm> getAllClients() {
        return this.clientService.getAllClients();
    }

    @GetMapping("/documents/{id}")
    public List<DocumentSafeForm> getClientDocuments(@PathVariable Long id){return this.clientService.getClientDocuments(id);}

    @GetMapping("/rut/{rut}")
    public ClientEntity getClientByRut(@PathVariable Integer rut) {  return this.clientService.getClientByRut(rut);}

    @GetMapping("/{id}")
    public ClientGetForm getClientById(@PathVariable Long id) {  return this.clientService.getClientById(id);}

    @GetMapping("/rinfo/{rut}")
    public ClientInfoRequiredForm getClienRequiredInfoByRut(@PathVariable Integer rut) {
        return this.clientService.getClientRequiredInfoByRut(rut);
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updateClient(@RequestBody ClientEntity client) {
        return this.clientService.updateClient(client);
    }
}
