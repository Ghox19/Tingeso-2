package com.microservice.documentMicroservice.Services;

import com.microservice.documentMicroservice.DTOS.DocumentForm;
import com.microservice.documentMicroservice.DTOS.DocumentSafeForm;
import com.microservice.documentMicroservice.Entities.DocumentEntity;
import com.microservice.documentMicroservice.Repositories.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    public Long saveDocument(DocumentForm documentForm) {
        DocumentEntity documentEntity = new DocumentEntity();
        documentEntity.setName(documentForm.getName());

        // Decode Base64 content
        byte[] contentBytes = Base64.getDecoder().decode(documentForm.getContent());
        documentEntity.setContent(contentBytes);

        documentEntity.setType(documentForm.getType());
        documentEntity.setApproved(documentForm.getApproved());
        documentEntity.setClientId(documentForm.getClientId());
        documentEntity.setClientLoanId(documentForm.getClienLoanId());

        documentRepository.save(documentEntity);

        long id = documentEntity.getId();
        // Save the document entity
        return id;
    }

    public List<DocumentEntity> getAll() {
        return documentRepository.findAll();
    }

    public DocumentSafeForm getDocument(Long id) {
        Optional<DocumentEntity> document = documentRepository.findById(id);
        return document.map(this::setDocumentSafeForm).orElse(null);
    }

    public DocumentEntity getDocumentRaw(Long id) {
        return documentRepository.findById(id).orElse(null);
    }

    public DocumentEntity returnJSON(MultipartFile file) throws IOException {
        DocumentEntity document = new DocumentEntity();
        document.setName(file.getOriginalFilename());
        document.setContent(file.getBytes());
        return document;
    }

    public DocumentSafeForm setDocumentSafeForm(DocumentEntity document){
        DocumentSafeForm documentSafeForm = new DocumentSafeForm();
        documentSafeForm.setId(document.getId());
        documentSafeForm.setName(document.getName());
        documentSafeForm.setType(document.getType());
        documentSafeForm.setApproved(document.getApproved());
        return documentSafeForm;
    }

    public DocumentEntity updateDocument(Long id, DocumentForm documentForm) {
        DocumentEntity existingDocument = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found with id: " + id));

        existingDocument.setName(documentForm.getName());

        // Update content only if new content is provided
        if (documentForm.getContent() != null) {
            byte[] contentBytes = Base64.getDecoder().decode(documentForm.getContent());
            existingDocument.setContent(contentBytes);
        }

        existingDocument.setType(documentForm.getType());
        existingDocument.setApproved(documentForm.getApproved());

        return documentRepository.save(existingDocument);
    }
}
