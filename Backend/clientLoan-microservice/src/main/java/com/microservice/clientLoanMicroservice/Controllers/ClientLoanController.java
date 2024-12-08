package com.microservice.clientLoanMicroservice.Controllers;


import com.microservice.clientLoanMicroservice.DTOS.ClientLoanForm;
import com.microservice.clientLoanMicroservice.DTOS.ClientLoanGetForm;
import com.microservice.clientLoanMicroservice.Entities.ClientLoanEntity;
import com.microservice.clientLoanMicroservice.Services.ClientLoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/clientLoan")
public class ClientLoanController {
    private final ClientLoanService clientLoanService;

    @Autowired
    public ClientLoanController(ClientLoanService clientLoanService) {
        this.clientLoanService = clientLoanService;
    }

    @PostMapping
    public ResponseEntity<Object> addClientLoan(@RequestBody ClientLoanForm clientLoanForm) {
        return this.clientLoanService.addClientLoan(clientLoanForm);
    }

    @GetMapping("/{id}")
    public ClientLoanGetForm getClientLoansById(@PathVariable Long id) {
        return this.clientLoanService.getClientLoanById(id);
    }

    @GetMapping
    public List<ClientLoanGetForm> getAllClientLoan(){
        return this.clientLoanService.getAllClientLoan();
    }

    @PutMapping("/saving")
    public ResponseEntity<Object> updateSaving(@RequestBody ClientLoanGetForm clientLoanGetForm){
        return this.clientLoanService.updateClientLoanSavingId(clientLoanGetForm);
    }

    @GetMapping("/raw/{id}")
    public ClientLoanEntity getClientLoansRawById(@PathVariable Long id) {
        return this.clientLoanService.getClientLoanRawById(id);
    }
}
