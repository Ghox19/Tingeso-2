package com.microservice.simulationMicroservice.Controllers;

import com.microservice.simulationMicroservice.Entities.CalculatorForm;
import com.microservice.simulationMicroservice.Services.SimulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/simulation")
public class SimulationController {
    private final SimulationService simulationService;

    @Autowired
    public SimulationController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @PostMapping
    public Integer getMonthlyPay(@RequestBody CalculatorForm calculatorForm) {
        return this.simulationService.calculateMensualPay(calculatorForm);
    }
}
