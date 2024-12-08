package com.microservice.savingMicroservice.Controllers;

import com.microservice.savingMicroservice.DTOs.SavingForm;
import com.microservice.savingMicroservice.Entities.SavingEntity;
import com.microservice.savingMicroservice.Services.SavingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/saving")
public class SavingController {

    private final SavingService savingService;

    @Autowired
    public SavingController(SavingService savingService) {
        this.savingService = savingService;
    }

    @PostMapping
    public Long addSaving(@RequestBody SavingForm savingForm) {
        return savingService.addSaving(savingForm);
    }

    @GetMapping("/{id}")
    public SavingEntity getSavingById(@PathVariable Long id) {return savingService.getSavingById(id);}
}
