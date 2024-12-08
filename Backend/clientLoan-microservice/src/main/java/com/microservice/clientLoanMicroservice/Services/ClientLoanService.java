package com.microservice.clientLoanMicroservice.Services;

import com.microservice.clientLoanMicroservice.DTOS.*;
import com.microservice.clientLoanMicroservice.Entities.ClientEntity;
import com.microservice.clientLoanMicroservice.Entities.ClientLoanEntity;
import com.microservice.clientLoanMicroservice.Entities.LoanEntity;
import com.microservice.clientLoanMicroservice.Entities.TracingEntity;
import com.microservice.clientLoanMicroservice.Repositories.ClientLoanRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientLoanService {

    @Autowired
    private ClientLoanRepository clientLoanRepository;
    @Autowired
    private RestTemplate restTemplate;
    private static final String CLIENT_SERVICE_URL = "http://client-microservice/client";
    private static final String DOCUMENT_SERVICE_URL = "http://document-microservice/document";
    private static final String LOAN_SERVICE_URL = "http://loan-microservice/loan";
    private static final String TRACING_SERVICE_URL = "http://tracing-microservice/tracing";

    public ResponseEntity<Object> addClientLoan(ClientLoanForm clientLoanForm) {
        ClientEntity client = getClientByRutFromMicroservice(clientLoanForm.getRut());

        if (client == null) {
            return ResponseEntity
                    .badRequest()
                    .body("No se encontró el cliente con el RUT especificado");
        }

        if (clientLoanForm.getRut() == null || clientLoanForm.getYears() == null ||
                clientLoanForm.getInterest() == null || clientLoanForm.getLoanAmount() == null) {
            return ResponseEntity
                    .badRequest()
                    .body("Faltan datos requeridos");
        }

        // Validate monthly payment calculation
        double mensualPay = clientLoanForm.getMensualPay();
        if (mensualPay == 0) { // Assuming 0 represents "Valor obtenido"
            return ResponseEntity
                    .badRequest()
                    .body("Falta calcular el valor de la cuota mensual");
        }

        LoanEntity loantype = getLoanByNameFromMicroservice(clientLoanForm.getLoanName());
        if (loantype == null){
            return ResponseEntity
                    .badRequest()
                    .body("No se conecto correctamente el loan");
        }

        if (clientLoanForm.getInterest() < loantype.getMinInterest() || clientLoanForm.getInterest() > loantype.getMaxInterest()){
            return ResponseEntity
                    .badRequest()
                    .body("El Interes del prestamo esta fuera de los limites");
        }

        if (clientLoanForm.getYears() > loantype.getMaxYears()){
            return ResponseEntity
                    .badRequest()
                    .body("Los años del prestamo esta fuera de los limites");
        }

        float loanRatio = (float) (clientLoanForm.getLoanAmount() * 100) / clientLoanForm.getPropertyValue();
        if (loanRatio > loantype.getMaxAmount()){
            return ResponseEntity
                    .badRequest()
                    .body("La cantidad del prestamo esta fuera de los limites");
        }

        if (clientLoanForm.getDocuments().size() < loantype.getRequirements().size()){
            return ResponseEntity
                    .badRequest()
                    .body("Faltan Documentos");
        }
        // Check monthly payment to income ratio
        double cuotaIncome = (mensualPay / client.getMensualIncome()) * 100;
        if (cuotaIncome > 35) {
            return ResponseEntity
                    .badRequest()
                    .body("La cuota mensual excede el 35% del sueldo");
        }

        // Validate job years
        if (client.getJobYears() < 1) {
            return ResponseEntity
                    .badRequest()
                    .body("El cliente no tiene la antigüedad laboral mínima requerida (1 año)");
        }

        // Calculate and validate total debt ratio
        double totalDebt = client.getTotalDebt() + mensualPay;
        double debtCuota = (totalDebt / client.getMensualIncome()) * 100;
        if (debtCuota > 50) {
            return ResponseEntity
                    .badRequest()
                    .body("La deuda total excede el 50% del sueldo mensual");
        }


        // Validate age limit
        int totalYears = client.getYears() + clientLoanForm.getYears();
        if (totalYears > 70) {
            return ResponseEntity
                    .badRequest()
                    .body("El cliente excede el límite de edad permitido (70 años)");
        }


        ClientLoanEntity clientLoan = createAndSaveClientLoan(clientLoanForm, client.getId(), loanRatio);
        Long tracingId = createAndSaveTracing(cuotaIncome, debtCuota, clientLoan.getId(), clientLoanForm.getMensualPay(), clientLoanForm.getFase());
        clientLoan.setTracingId(tracingId);
        List<Long> documents = processDocuments(clientLoanForm.getDocuments(), clientLoan.getId());

        updateClientWithLoan(client, clientLoan, documents);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Se ingresó correctamente el préstamo");
    }

    private ClientEntity getClientByRutFromMicroservice(Integer rut) {
        try {
            ResponseEntity<ClientEntity> response = restTemplate.getForEntity(
                    CLIENT_SERVICE_URL + "/rut/" + rut,
                    ClientEntity.class
            );
            return response.getBody();
        } catch (RestClientException e) {
            return null;
        }
    }

    private LoanEntity getLoanByNameFromMicroservice(String loanName) {
        try {
            ResponseEntity<LoanEntity> response = restTemplate.getForEntity(
                    LOAN_SERVICE_URL + "/" + loanName,
                    LoanEntity.class
            );
            return response.getBody();
        } catch (RestClientException e) {
            return null;
        }
    }

    private ClientLoanEntity createAndSaveClientLoan(ClientLoanForm form, Long clientId, Float loanRatio) {
        ClientLoanEntity clientLoan = new ClientLoanEntity();
        clientLoan.setClientId(clientId);
        setClientLoanFields(clientLoan, form);
        clientLoan.setLoanRatio(loanRatio);
        return this.clientLoanRepository.save(clientLoan);
    }

    private Long createAndSaveTracing(Double cuotaIncome, Double debtCuota, Long clientLoanId, Integer mensualPay, String fase) {
        TracingEntity tracingEntity = new TracingEntity();
        tracingEntity.setClientLoanId(clientLoanId);
        tracingEntity.setCuotaIncome(cuotaIncome);
        tracingEntity.setDebtCuota(debtCuota);
        tracingEntity.setMensualPay(mensualPay);
        tracingEntity.setFase(fase);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TracingEntity> requestEntity = new HttpEntity<>(tracingEntity, headers);

        ResponseEntity<Long> response = restTemplate.postForEntity(
                TRACING_SERVICE_URL,
                requestEntity,
                Long.class
        );

        return response.getBody();
    }

    private void setClientLoanFields(ClientLoanEntity clientLoan, ClientLoanForm form) {
        clientLoan.setLoanAmount(form.getLoanAmount());
        clientLoan.setLoanName(form.getLoanName());
        clientLoan.setInterest(form.getInterest());
        clientLoan.setYears(form.getYears());
        clientLoan.setPropertyValue(form.getPropertyValue());
    }

    private List<Long> processDocuments(List<DocumentForm> documentForms, Long clientLoanId) {
        List<Long> documentIds = new ArrayList<>();

        for (DocumentForm docForm : documentForms) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            docForm.setClienLoanId(clientLoanId);

            HttpEntity<DocumentForm> requestEntity = new HttpEntity<>(docForm, headers);

            Long documentId = restTemplate.postForObject(
                    DOCUMENT_SERVICE_URL,
                    requestEntity,
                    Long.class
            );

            documentIds.add(documentId);
        }

        return documentIds;
    }

    private void updateClientWithLoan(ClientEntity client, ClientLoanEntity clientLoan,
                                      List<Long> documentsId) {
        try {
            clientLoan.setDocumentsId(documentsId);
            this.clientLoanRepository.save(clientLoan);

            client.getLoansId().add(clientLoan.getId());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<ClientEntity> requestEntity = new HttpEntity<>(client, headers);

            restTemplate.put(
                    CLIENT_SERVICE_URL + "/update",
                    requestEntity,
                    ClientEntity.class
            );
        } catch (RestClientException e) {
            throw new RuntimeException("Error al actualizar el cliente: " + e.getMessage());
        }
    }

    public ClientLoanGetForm getClientLoanById(Long id) {
        Optional<ClientLoanEntity> clientLoan = clientLoanRepository.findById(id);

        if (clientLoan.isPresent()) {
            return setClientLoanGetForm(clientLoan.get());
        } else {
            throw new EntityNotFoundException("Client Loan not found with id: " + id);
        }
    }

    public List<ClientLoanGetForm> getAllClientLoan() {
        List<ClientLoanEntity> clientLoans = this.clientLoanRepository.findAll();

        return clientLoans.stream()
                .map(this::setClientLoanGetForm)
                .collect(Collectors.toList());
    }

    public ClientLoanGetForm setClientLoanGetForm(ClientLoanEntity clientLoan){
        ClientLoanGetForm clientLoanGetForm = new ClientLoanGetForm();
        clientLoanGetForm.setId(clientLoan.getId());
        clientLoanGetForm.setInterest(clientLoan.getInterest());
        clientLoanGetForm.setLoanName(clientLoan.getLoanName());
        clientLoanGetForm.setYears(clientLoan.getYears());
        clientLoanGetForm.setLoanAmount(clientLoan.getLoanAmount());
        clientLoanGetForm.setClientId(clientLoan.getClientId());
        clientLoanGetForm.setSavingsId(clientLoan.getSavingId());
        clientLoanGetForm.setPropertyValue(clientLoan.getPropertyValue());
        clientLoanGetForm.setLoanRatio(clientLoan.getLoanRatio());

        TracingEntity tracingEntity = getTracingById(clientLoan.getTracingId());

        if (tracingEntity == null){
            return null;
        }

        clientLoanGetForm.setMensualPay(tracingEntity.getMensualPay());
        clientLoanGetForm.setFase(tracingEntity.getFase());
        clientLoanGetForm.setCuotaIncome(tracingEntity.getCuotaIncome());
        clientLoanGetForm.setDebtCuota(tracingEntity.getDebtCuota());
        clientLoanGetForm.setMessage(tracingEntity.getMessage());
        clientLoanGetForm.setFireInsurance(tracingEntity.getFireInsurance());
        clientLoanGetForm.setDeduction(tracingEntity.getDeduction());
        clientLoanGetForm.setTotalCost(tracingEntity.getTotalCost());

        List<DocumentSafeForm> documentForms = getDocumentForms(clientLoan.getDocumentsId());

        clientLoanGetForm.setDocuments(documentForms);

        return clientLoanGetForm;
    }

    private List<DocumentSafeForm> getDocumentForms(List<Long> documentIds) {
        return documentIds.stream()
                .map(docId -> {
                    try {
                        ResponseEntity<DocumentSafeForm> response = restTemplate.getForEntity(
                                DOCUMENT_SERVICE_URL + "/" + docId,
                                DocumentSafeForm.class
                        );
                        return response.getBody();
                    } catch (RestClientException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private TracingEntity getTracingById(Long id) {
        try {
            ResponseEntity<TracingEntity> response = restTemplate.getForEntity(
                    TRACING_SERVICE_URL + "/" + id,
                    TracingEntity.class
            );

            return response.getBody();
        } catch (RestClientException e) {
            return null;
        }
    }

    public ResponseEntity<Object> updateClientLoanSavingId(ClientLoanGetForm clientLoanGetForm){
        Optional<ClientLoanEntity> clientLoan = clientLoanRepository.findById(clientLoanGetForm.getId());

        if (clientLoan.isPresent()) {
            ClientLoanEntity clientLoanEntity = clientLoan.get();
            clientLoanEntity.setSavingId(clientLoanGetForm.getSavingsId());
            this.clientLoanRepository.save(clientLoanEntity);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("Se actualizo el SavingId");
        } else {
            return ResponseEntity
                    .badRequest()
                    .body("No se encontró el credito ingresado");
        }
    }

    public ClientLoanEntity getClientLoanRawById(Long id){
        Optional<ClientLoanEntity> clientLoan = clientLoanRepository.findById(id);

        if (clientLoan.isPresent()) {
            return clientLoan.get();
        } else {
            throw new EntityNotFoundException("Client Loan not found with id: " + id);
        }
    }
}
