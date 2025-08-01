package com.company.hr.system.service;

import com.company.hr.system.dto.DocumentMetadataDto;
import com.company.hr.system.dto.DocumentUploadRequest;
import com.company.hr.system.model.Document;
import com.company.hr.system.model.DocumentTypeEnum;
import com.company.hr.system.model.Employee;
import com.company.hr.system.repository.DocumentRepository;
import com.company.hr.system.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final EmployeeRepository employeeRepository;

    @Transactional
    public Document createDocument(DocumentUploadRequest request) throws IOException {
        // Validate employee exists
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));

        // Validate file
        validateFile(request.getFile(), request.getType());

        // Build info string based on document type
        String info = buildInfoString(request);

        return documentRepository.save(Document.builder()
                .employee(employee)
                .type(request.getType())
                .fileName(request.getFile().getOriginalFilename())
                .mimeType(request.getFile().getContentType())
                .fileContent(request.getFile().getBytes())
                .fileSize(request.getFile().getSize())
                .issueDate(request.getIssueDate())
                .expiryDate(request.getExpiryDate())
                .info(info)
                .build());
    }

    private String buildInfoString(DocumentUploadRequest request) {
        return switch (request.getType()) {
            case NATIONAL_ID -> request.getIssuingAuthority();
            case CONTRACT -> request.getContractVersion();
            case CERTIFICATION -> String.format("%s|%s",
                    request.getCertificationName(),
                    request.getIssuingBody());
        };
    }

    private void validateFile(MultipartFile file, DocumentTypeEnum type) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        }

        String originalFilename = file.getOriginalFilename();

        String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();

        switch (type) {
            case CONTRACT -> {
                if (!"pdf".equals(extension)) {
                    throw new IllegalArgumentException("Contract must be a PDF file");
                }
            }
            case NATIONAL_ID, CERTIFICATION -> {
                if (!Set.of("pdf", "jpg", "jpeg", "png").contains(extension)) {
                    throw new IllegalArgumentException("National ID/Certification must be PDF, JPG, or PNG");
                }
            }
            default -> throw new IllegalArgumentException("Unsupported document type: " + type);
        }
    }

    @Transactional(readOnly = true)
    public List<DocumentMetadataDto> getDocumentMetadataByEmployeeId(Long employeeId) {
        List<Document> documents = documentRepository.findByEmployeeId(employeeId);
        return documents.stream()
                .map(this::convertToMetadataDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public DocumentMetadataDto getDocumentMetadataById(Long documentId) {
        Document document = getDocumentById(documentId);
        return convertToMetadataDto(document);
    }

    @Transactional(readOnly = true)
    public Document getDocumentById(Long documentId) {
        return documentRepository.findById(Math.toIntExact(documentId))
                .orElseThrow(() -> new IllegalArgumentException("Document not found"));
    }

    private DocumentMetadataDto convertToMetadataDto(Document document) {
        DocumentMetadataDto.DocumentMetadataDtoBuilder builder = DocumentMetadataDto.builder()
                .id(document.getId())
                .type(document.getType())
                .fileName(document.getFileName())
                .uploadDate(document.getUploadDate());

        if (document.getInfo() != null && !document.getInfo().isEmpty()) {
            switch (document.getType()) {
                case NATIONAL_ID:
                    builder.issuingAuthority(document.getInfo());
                    break;
                case CONTRACT:
                    builder.contractVersion(document.getInfo());
                    break;
                case CERTIFICATION:
                    String[] parts = document.getInfo().split("\\|");
                    if (parts.length >= 1) {
                        builder.certificationName(parts[0]);
                    }
                    if (parts.length >= 2) {
                        builder.issuingBody(parts[1]);
                    }
                    break;
            }
        }

        return builder.build();
    }
}
