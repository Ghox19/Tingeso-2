package com.microservice.documentMicroservice.Controllers;

import com.microservice.documentMicroservice.DTOS.DocumentForm;
import com.microservice.documentMicroservice.DTOS.DocumentSafeForm;
import com.microservice.documentMicroservice.Entities.DocumentEntity;
import com.microservice.documentMicroservice.Services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/document")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @PostMapping
    public ResponseEntity<Long> createDocument(@RequestBody DocumentForm request) {
        Long documentId = documentService.saveDocument(request);
        return new ResponseEntity<>(documentId, HttpStatus.CREATED);
    }

    @PostMapping("/jsonConvert")
    public DocumentEntity returnJSON(@RequestParam("file") MultipartFile file) {
        try {
            return documentService.returnJSON(file);
        } catch (IOException e) {
            return null;
        }
    }

    @GetMapping
    public List<DocumentEntity> getAllDocument(){
        return documentService.getAll();
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable Long id) {
        DocumentEntity document = documentService.getDocumentRaw(id);
        if (document != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.builder("attachment")
                    .filename(document.getName())
                    .build());

            return new ResponseEntity<>(document.getContent(), headers, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentSafeForm> getDocument(@PathVariable Long id) {
        DocumentSafeForm document = documentService.getDocument(id);
        if (document != null) {
            return ResponseEntity.ok(document);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocumentEntity> updateDocument(
            @PathVariable Long id,
            @RequestBody DocumentForm documentForm) {
        try {
            DocumentEntity updatedDocument = documentService.updateDocument(id, documentForm);
            return new ResponseEntity<>(updatedDocument, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}