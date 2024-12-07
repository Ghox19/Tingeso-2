package com.microservice.loanMicroservice.Controllers;

import com.microservice.loanMicroservice.Entities.LoanEntity;
import com.microservice.loanMicroservice.Services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/loan")
public class LoanController {
    private final LoanService loanService;

    @Autowired
    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @GetMapping
    public List<LoanEntity> getAllLoans() { return this.loanService.getAllLoans();}

    @GetMapping("/{name}")
    public LoanEntity getLoanByName(@PathVariable String name) { return this.loanService.getLoanByName(name);}
}
