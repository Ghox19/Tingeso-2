package com.microservice.loanMicroservice.Services;

import com.microservice.loanMicroservice.Entities.LoanEntity;
import com.microservice.loanMicroservice.Repositories.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanService {

    private final LoanRepository loanRepository;

    @Autowired
    public LoanService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    public List<LoanEntity> getAllLoans(){
        return this.loanRepository.findAll();
    }

    public LoanEntity getLoanByName(String name){return this.loanRepository.findByName(name);}
}
