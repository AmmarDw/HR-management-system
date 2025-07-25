package com.company.hr.system.repository;

import com.company.hr.system.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Integer> {
    List<Document> findByEmployeeId(Long employeeId);
}
