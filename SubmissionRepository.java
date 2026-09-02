package com.example.demo.repository;

import com.example.demo.model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    boolean existsByTeamNameIgnoreCase(String teamName);
}
