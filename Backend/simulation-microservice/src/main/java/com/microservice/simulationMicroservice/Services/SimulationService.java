package com.microservice.simulationMicroservice.Services;

import com.microservice.simulationMicroservice.Entities.CalculatorForm;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class SimulationService {
    public Integer calculateMensualPay (CalculatorForm calculatorForm){
        BigDecimal monthlyInterest = BigDecimal.valueOf(calculatorForm.getInterest())
                .divide(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);
        int totalMonths = calculatorForm.getYears() * 12;

        BigDecimal numerator = monthlyInterest.multiply(
                BigDecimal.ONE.add(monthlyInterest).pow(totalMonths)
        );
        BigDecimal denominator = BigDecimal.ONE.add(monthlyInterest).pow(totalMonths)
                .subtract(BigDecimal.ONE);

        BigDecimal monthlyPayment = BigDecimal.valueOf(calculatorForm.getLoanAmount())
                .multiply(numerator)
                .divide(denominator, 0, RoundingMode.HALF_UP);

        return monthlyPayment.intValue();
    }
}
