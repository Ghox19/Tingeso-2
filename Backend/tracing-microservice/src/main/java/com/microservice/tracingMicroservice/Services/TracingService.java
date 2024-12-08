package com.microservice.tracingMicroservice.Services;

import com.microservice.tracingMicroservice.DTOs.ClientLoanFinalApprovedForm;
import com.microservice.tracingMicroservice.DTOs.ClientLoanPreApprovedForm;
import com.microservice.tracingMicroservice.DTOs.ClientLoanRejectForm;
import com.microservice.tracingMicroservice.Entities.ClientLoanEntity;
import com.microservice.tracingMicroservice.Entities.TracingEntity;
import com.microservice.tracingMicroservice.Repositories.TracingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class TracingService {
    @Autowired
    private TracingRepository tracingRepository;
    @Autowired
    private RestTemplate restTemplate;
    private static final String CLIENTLOAN_SERVICE_URL = "http://clientloan-microservice/clientLoan";

    public Long addTracing(TracingEntity tracingEntity) {
        if (tracingEntity == null) {
            return null;
        }

        TracingEntity savedEntity = tracingRepository.save(tracingEntity);
        return savedEntity.getId();
    }

    public TracingEntity getTracingById(Long id){
        Optional<TracingEntity> optionalTracingEntity = tracingRepository.findById(id);
        return optionalTracingEntity.orElse(null);
    }

    public ResponseEntity<Object> updateClientLoanPreApproved(ClientLoanPreApprovedForm form){
        ClientLoanEntity clientLoan = getClientLoanByIdFromMicroservice(form.getClientLoanId());

        if (clientLoan == null){
            return ResponseEntity
                    .badRequest()
                    .body("No se encontro la Solicitud");
        }

        Optional<TracingEntity> tracingEntityOptional = this.tracingRepository.findById(clientLoan.getTracingId());

        if (tracingEntityOptional.isEmpty()){
            return ResponseEntity
                    .badRequest()
                    .body("No se encontro el tracing");
        }

        TracingEntity tracingEntity = tracingEntityOptional.get();

        double deductionAmount = clientLoan.getLoanAmount() * form.getDeduction();
        double commission = clientLoan.getLoanAmount() * 0.01;
        int newMensualPay = (int) (tracingEntity.getMensualPay() + deductionAmount + form.getFireInsurance());
        tracingEntity.setMensualPay(newMensualPay);
        tracingEntity.setDeduction(form.getDeduction());
        tracingEntity.setFireInsurance(form.getFireInsurance());

        int totalMonths = clientLoan.getYears() * 12;
        int totalCost = (int) ((totalMonths * newMensualPay) + commission);
        tracingEntity.setTotalCost(totalCost);
        tracingEntity.setFase("Pre-Aprobada");
        tracingRepository.save(tracingEntity);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Se PreAprobo Correctamente el Prestamo");
    }

    private ClientLoanEntity getClientLoanByIdFromMicroservice(Long id) {
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

    public ResponseEntity<Object> updateFinalApproved(ClientLoanFinalApprovedForm form){
        Optional<TracingEntity> tracingEntityOptional = tracingRepository.findByClientLoanId(form.getId());

        if (tracingEntityOptional.isEmpty()){
            return ResponseEntity
                    .badRequest()
                    .body("No se encontro la Solicitud");
        }

        TracingEntity tracingEntity = tracingEntityOptional.get();

        if (form.getFase().equals("Desembolso")){
            tracingEntity.setFase(form.getFase());
            tracingRepository.save(tracingEntity);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("Se Desembolso Correctamente el Prestamo");
        } else {
            tracingEntity.setFase(form.getFase());
            tracingEntity.setMessage("Rechazado porque el cliente no acepto las condiciones");
            tracingRepository.save(tracingEntity);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("Se Desembolso Correctamente el Prestamo");
        }
    }

    public ResponseEntity<Object> updateReject(ClientLoanRejectForm form){
        Optional<TracingEntity> tracingEntityOptional = tracingRepository.findByClientLoanId(form.getId());

        if (tracingEntityOptional.isEmpty()){
            return ResponseEntity
                    .badRequest()
                    .body("No se encontro la Solicitud");
        }

        TracingEntity tracingEntity = tracingEntityOptional.get();
        tracingEntity.setFase("Rechazado");
        tracingEntity.setMessage(form.getMessage());
        tracingRepository.save(tracingEntity);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Se Rechazo Correctamente el Prestamo");
    }
}
