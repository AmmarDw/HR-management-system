package com.company.hr.system.controller;

import com.company.hr.system.dto.DocumentMetadataDto;
import com.company.hr.system.dto.DocumentUploadRequest;
import com.company.hr.system.model.Document;
import com.company.hr.system.model.DocumentTypeEnum;
import com.company.hr.system.service.DocumentService;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/document")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Document> uploadDocument(
            @Valid @ModelAttribute DocumentUploadRequest request) throws IOException {

        // Validate certification name when type is CERTIFICATION
        if (request.getType() == DocumentTypeEnum.CERTIFICATION &&
                StringUtils.isBlank(request.getCertificationName())) {
            throw new IllegalArgumentException("Certification name is required");
        }

        // Process the document based on type
        Document document = documentService.createDocument(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(document);
    }

    @GetMapping("/view")
    public ResponseEntity<List<DocumentMetadataDto>> getDocumentsByEmployee(
            @RequestParam(required = false) Long employeeId,
            @RequestParam(required = false) Long documentId) {

        if (documentId != null) {
            DocumentMetadataDto document = documentService.getDocumentMetadataById(documentId);
            return ResponseEntity.ok(List.of(document));
        } else if (employeeId != null) {
            List<DocumentMetadataDto> documents = documentService.getDocumentMetadataByEmployeeId(employeeId);
            return ResponseEntity.ok(documents);
        } else {
            throw new IllegalArgumentException("Must provide either employeeId or documentId");
        }
    }

    @GetMapping("/{documentId}/download")
    public ResponseEntity<Resource> downloadDocument(
            @PathVariable Long documentId) {

        Document document = documentService.getDocumentById(documentId);

        ByteArrayResource resource = new ByteArrayResource(document.getFileContent());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(document.getMimeType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + document.getFileName() + "\"")
                .body(resource);
    }
}
