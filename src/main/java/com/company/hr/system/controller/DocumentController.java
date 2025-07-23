package com.company.hr.system.controller;

import com.company.hr.system.dto.DocumentUploadRequest;
import com.company.hr.system.model.Document;
import com.company.hr.system.model.DocumentTypeEnum;
import com.company.hr.system.service.DocumentService;
import com.company.hr.system.service.EmployeeService;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

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
}
