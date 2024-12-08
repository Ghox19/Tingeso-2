package com.microservice.savingMicroservice.Services;

import com.microservice.savingMicroservice.DTOs.ClientLoanGetForm;
import com.microservice.savingMicroservice.DTOs.ClientLoanRejectForm;
import com.microservice.savingMicroservice.DTOs.SavingForm;
import com.microservice.savingMicroservice.DTOs.SavingResultForm;
import com.microservice.savingMicroservice.Entities.ClientEntity;
import com.microservice.savingMicroservice.Entities.ClientLoanEntity;
import com.microservice.savingMicroservice.Entities.SavingEntity;
import com.microservice.savingMicroservice.Repositories.SavingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class SavingService {
    @Autowired
    private SavingRepository savingRepository;
    @Autowired
    private RestTemplate restTemplate;
    private static final String CLIENT_SERVICE_URL = "http://client-microservice/client";
    private static final String CLIENTLOAN_SERVICE_URL = "http://clientloan-microservice/clientLoan";
    private static final String TRACING_SERVICE_URL = "http://tracing-microservice/tracing";
    public Long addSaving(SavingForm savingForm){
        ClientLoanGetForm clientLoan = getClientLoanByIdFromMicroservice(savingForm.getClientLoanId());

        if (clientLoan == null){
            return null;
        }

        SavingEntity savingEntity = createSavingFromForm(savingForm, clientLoan.getClientId());
        List<String> reasons = verifyConditions(savingEntity, clientLoan);

        if (reasons == null){
            return null;
        }

        savingEntity.setReasons(reasons);

        int count = reasons.size();
        if (count > 3){
            savingEntity.setResult("Rechazado");
            clientLoan.setFase("Rechazado");
            clientLoan.setMessage("El Prestamo fue Rechazado por no cumplir correctamente con la Cuenta de Ahorros");
        } else if (count < 3 && count >= 1){
            savingEntity.setResult("Revision Adicional");
        } else {
            savingEntity.setResult("Aprobado");
        }

        this.savingRepository.save(savingEntity);

        clientLoan.setSavingsId(savingEntity.getId());

        updateSavingIdInClientLoan(clientLoan);

        return savingEntity.getId();
    }

    private SavingEntity createSavingFromForm(SavingForm form, Long clientLoanId) {
        SavingEntity saving = new SavingEntity();
        saving.setActualBalance(form.getActualBalance());
        saving.setBalances(form.getBalances());
        saving.setDeposit(form.getDeposit());
        saving.setYears(form.getYears());
        saving.setWithdraw(form.getWithdraw());
        saving.setClientLoanId(clientLoanId);
        return saving;
    }

    List<String> verifyConditions(SavingEntity saving, ClientLoanGetForm clientLoan){
        List<String> reasons = new ArrayList<>();

        double cuota = clientLoan.getLoanAmount() * 0.10;
        if (saving.getActualBalance() < cuota){
            reasons.add("R71: No cumple con el saldo mínimo requerido (10% del préstamo).");
        }

        Integer[] withdrawsArray =  saving.getWithdraw().toArray(new Integer[0]);
        Integer[] balancesArray =  saving.getBalances().toArray(new Integer[0]);
        boolean history = verificarHistorialAhorro(balancesArray, withdrawsArray);
        if (!history){
            reasons.add("R72: No mantiene un historial de ahorro consistente.");
        }

        ClientEntity client = getClientByIdFromMicroservice(clientLoan.getClientId());

        if (client == null){
            return null;
        }
        boolean regularIncome =  verificarDepositosPeriodicos(saving.getDeposit(), client.getMensualIncome());
        if (!regularIncome){
            reasons.add("R73: No cumple con los depósitos periódicos requeridos.");
        }

        if (saving.getYears() < 2){
            cuota = clientLoan.getLoanAmount() * 0.20;
            if (saving.getActualBalance() < cuota){
                reasons.add("R74: No cumple con la relación saldo/antigüedad requerida.");
            }
        }

        boolean recentWithdraw = verificarRetirosRecientes(withdrawsArray, saving.getActualBalance());
        if (!recentWithdraw){
            reasons.add("R75: Ha realizado retiros significativos recientemente.");
        }

        return reasons;
    }

    private ClientLoanGetForm getClientLoanByIdFromMicroservice(Long id) {
        try {
            ResponseEntity<ClientLoanGetForm> response = restTemplate.getForEntity(
                    CLIENTLOAN_SERVICE_URL + "/" + id,
                    ClientLoanGetForm.class
            );
            return response.getBody();
        } catch (RestClientException e) {
            return null;
        }
    }

    private ClientLoanEntity getClientLoanRawByIdFromMicroservice(Long id) {
        try {
            ResponseEntity<ClientLoanEntity> response = restTemplate.getForEntity(
                    CLIENTLOAN_SERVICE_URL + "/raw/" + id,
                    ClientLoanEntity.class
            );
            return response.getBody();
        } catch (RestClientException e) {
            return null;
        }
    }

    private ClientEntity getClientByIdFromMicroservice(Long id) {
        try {
            ResponseEntity<ClientEntity> response = restTemplate.getForEntity(
                    CLIENT_SERVICE_URL + "/" + id,
                    ClientEntity.class
            );
            return response.getBody();
        } catch (RestClientException e) {
            return null;
        }
    }

    private ResponseEntity<Object> updateSavingIdInClientLoan(ClientLoanGetForm clientLoanGetForm) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<ClientLoanGetForm> requestEntity = new HttpEntity<>(clientLoanGetForm, headers);

            ResponseEntity<Object> response = restTemplate.exchange(
                    CLIENTLOAN_SERVICE_URL + "/saving",
                    HttpMethod.PUT,
                    requestEntity,
                    Object.class
            );

            return response;
        } catch (RestClientException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating saving: " + e.getMessage());
        }
    }

    boolean verificarHistorialAhorro(Integer[] balances, Integer[] withdraws) {
        for (int i = 0; i < 12; i++) {
            if (balances[i] <= 0) {
                return false;
            }
            if (i > 0 && withdraws[i] > (balances[i-1] * 0.50)) {
                return false;
            }
        }
        return true;
    }

    boolean verificarDepositosPeriodicos(List<Integer> deposits, Integer ingresosMensuales) {
        int mesesConsecutivosSinDeposito = 0;
        double minimoMensual = ingresosMensuales * 0.05;

        // Recorremos los últimos 12 meses
        for (Integer deposito : deposits) {
            if (deposito < minimoMensual) {
                mesesConsecutivosSinDeposito++;

                // Si hay más de 3 meses consecutivos sin depósito, falla inmediatamente
                if (mesesConsecutivosSinDeposito > 3) {
                    return false;
                }
            } else {
                // Reinicia el contador de meses consecutivos si hay un depósito válido
                mesesConsecutivosSinDeposito = 0;
            }
        }

        // Verifica que no haya más de 3 meses en total sin depósitos
        return true;
    }

    private boolean verificarRetirosRecientes(Integer[] withdraws, Integer actualBalance) {
        // Verifica los últimos 6 meses
        for (int i = 6; i < 12; i++) {
            if (withdraws[i] > (actualBalance * 0.30)) {
                return false;
            }
        }
        return true;
    }

    public SavingEntity getSavingById(Long id){
        Optional<SavingEntity> savingEntity = this.savingRepository.findById(id);
        return savingEntity.orElse(null);
    }

    public ResponseEntity<Object>  updateStateSaving(SavingResultForm form){
        Optional<SavingEntity> savingEntity = savingRepository.findById(form.getId());

        if(savingEntity.isPresent()){
            if(Objects.equals(form.getResult(), "Aprobado")){
                savingEntity.get().setResult(form.getResult());
                savingRepository.save(savingEntity.get());
                return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body("Se Aprobo correctamente la cuenta");
            } else {
                savingEntity.get().setResult(form.getResult());
                savingRepository.save(savingEntity.get());

                ClientLoanEntity clientLoan = getClientLoanRawByIdFromMicroservice(savingEntity.get().getClientLoanId());

                ClientLoanRejectForm clientLoanRejectForm = new ClientLoanRejectForm();
                clientLoanRejectForm.setId(clientLoan.getTracingId());
                clientLoanRejectForm.setMessage("El Prestamo fue Rechazado por no cumplir correctamente con la Cuenta de Ahorros");

                rejectLoan(clientLoanRejectForm);

                return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body("Se Rechazo correctamente la cuenta");
            }

        } else {
            return ResponseEntity
                    .badRequest()
                    .body("No se encontro la Cuenta de Ahorros");
        }
    }

    public ResponseEntity<Object> rejectLoan(ClientLoanRejectForm form) {
        return restTemplate.exchange(
                TRACING_SERVICE_URL + "/reject",
                HttpMethod.PUT,
                new HttpEntity<>(form),
                Object.class
        );
    }
}
