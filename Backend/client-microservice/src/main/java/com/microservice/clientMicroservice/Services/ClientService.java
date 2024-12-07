package com.microservice.clientMicroservice.Services;

import com.microservice.clientMicroservice.DTOS.*;
import com.microservice.clientMicroservice.Entities.ClientEntity;
import com.microservice.clientMicroservice.Repositories.ClientRepository;
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
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private RestTemplate restTemplate;
    private static final String DOCUMENT_SERVICE_URL = "http://document-microservice/document";

    public ResponseEntity<Object> addClient(RegisterForm clientForm) {
        if (clientRepository.findByRut(clientForm.getRut()).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body("Ya existe un cliente con el RUT especificado");
        }

        ClientEntity newClient = createClientFromForm(clientForm);
        newClient = this.clientRepository.save(newClient);

        try {
            List<Long> documentIds = processDocuments(clientForm.getDocuments(), newClient.getId());
            newClient.setDocumentsId(documentIds);
            clientRepository.save(newClient);
        } catch (RestClientException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar los documentos");
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Se ingresó correctamente el Usuario");
    }

    private ClientEntity createClientFromForm(RegisterForm form) {
        ClientEntity client = new ClientEntity();
        client.setName(form.getName());
        client.setLastName(form.getLastName());
        client.setRut(form.getRut());
        client.setEmail(form.getEmail());
        client.setYears(form.getYears());
        client.setContact(form.getContact());
        client.setJobType(form.getJobType());
        client.setMensualIncome(form.getMensualIncome());
        client.setJobYears(form.getJobYears());
        client.setTotalDebt(form.getTotalDebt());
        client.setLoansId(new ArrayList<>());
        return client;
    }

    private List<Long> processDocuments(List<DocumentForm> documentForms, Long clientId) {
        List<Long> documentIds = new ArrayList<>();

        for (DocumentForm docForm : documentForms) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            docForm.setClientId(clientId);

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

    public List<ClientGetForm> getAllClients() {
        List<ClientEntity> clients = this.clientRepository.findAll();

        return clients.stream()
                .map(this::setClientGetForm)
                .collect(Collectors.toList());
    }

    public ClientGetForm setClientGetForm(ClientEntity client){
        ClientGetForm clientGetForm = new ClientGetForm();
        clientGetForm.setId(client.getId());
        clientGetForm.setName(client.getName());
        clientGetForm.setLastName(client.getLastName());
        clientGetForm.setRut(client.getRut());
        clientGetForm.setEmail(client.getEmail());
        clientGetForm.setYears(client.getYears());
        clientGetForm.setContact(client.getContact());
        clientGetForm.setJobType(client.getJobType());
        clientGetForm.setMensualIncome(client.getMensualIncome());
        clientGetForm.setJobYears(client.getJobYears());
        clientGetForm.setTotalDebt(client.getTotalDebt());

        List<DocumentSafeForm> documentForms = getDocumentForms(client.getDocumentsId());

        clientGetForm.setDocuments(documentForms);
        clientGetForm.setLoans(null);
        return clientGetForm;
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

    public ClientEntity getClientByRut(Integer rut){
        Optional<ClientEntity> client = this.clientRepository.findByRut(rut);
        return client.orElse(null);
    }

    public ClientGetForm getClientById(Long id) {
        Optional<ClientEntity> client = clientRepository.findById(id);

        if (client.isPresent()) {
            return setClientGetForm(client.get());
        } else {
            throw new EntityNotFoundException("Client Loan not found with id: " + id);
        }
    }

    public List<DocumentSafeForm> getClientDocuments(Long id){
        ClientGetForm client = getClientById(id);

        if (client != null){
            return client.getDocuments();
        }
        else {
            return null;
        }
    }

    public ClientInfoRequiredForm getClientRequiredInfoByRut(Integer rut){
        Optional<ClientEntity> optionalClient = clientRepository.findByRut(rut);

        if (optionalClient.isPresent()){
            ClientInfoRequiredForm clientInfoRequiredForm = new ClientInfoRequiredForm();
            clientInfoRequiredForm.setYears(optionalClient.get().getYears());
            clientInfoRequiredForm.setJobYears(optionalClient.get().getJobYears());
            clientInfoRequiredForm.setMensualIncome(optionalClient.get().getMensualIncome());
            clientInfoRequiredForm.setJobType(optionalClient.get().getJobType());
            clientInfoRequiredForm.setTotalDebt(optionalClient.get().getTotalDebt());
            return clientInfoRequiredForm;
        }

        return null;
    }

    public ResponseEntity<Object> updateClient(ClientEntity updateRequest) {
        Optional<ClientEntity> clientOptional = clientRepository.findById(updateRequest.getId());

        if (clientOptional.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Cliente no encontrado");
        }

        ClientEntity client = clientOptional.get();

        // Actualizar solo los campos no nulos
        if (updateRequest.getName() != null) {
            client.setName(updateRequest.getName());
        }
        if (updateRequest.getLastName() != null) {
            client.setLastName(updateRequest.getLastName());
        }
        if (updateRequest.getEmail() != null) {
            client.setEmail(updateRequest.getEmail());
        }
        if (updateRequest.getYears() != null) {
            client.setYears(updateRequest.getYears());
        }
        if (updateRequest.getContact() != null) {
            client.setContact(updateRequest.getContact());
        }
        if (updateRequest.getJobType() != null) {
            client.setJobType(updateRequest.getJobType());
        }
        if (updateRequest.getMensualIncome() != null) {
            client.setMensualIncome(updateRequest.getMensualIncome());
        }
        if (updateRequest.getJobYears() != null) {
            client.setJobYears(updateRequest.getJobYears());
        }
        if (updateRequest.getTotalDebt() != null) {
            client.setTotalDebt(updateRequest.getTotalDebt());
        }
        if (updateRequest.getDocumentsId() != null) {
            client.setDocumentsId(updateRequest.getDocumentsId());
        }
        if (updateRequest.getLoansId() != null) {
            client.setLoansId(updateRequest.getLoansId());
        }

        clientRepository.save(client);

        return ResponseEntity
                .ok()
                .body("Cliente actualizado exitosamente");
    }
}
