package com.example.demo.controller;

import com.example.demo.exception.*;
import com.example.demo.model.Submission;
import com.example.demo.repository.SubmissionRepository;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class SubmissionController {

    private final SubmissionRepository repository;

    // FIXED DEADLINE
    private final LocalDateTime DEADLINE = 
            LocalDateTime.of(2026, 3, 1, 23, 59);

    public SubmissionController(SubmissionRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Submission submitProject(@RequestBody Submission submission) {

        // 1. Deadline Check
        if (LocalDateTime.now().isAfter(DEADLINE)) {
            throw new SubmissionClosedException(
                    "The hackathon deadline has passed. Submissions are closed."
            );
        }

        // 2. Github Validation
        if (submission.getGithubRepoUrl() == null || 
            !submission.getGithubRepoUrl().contains("github.com")) {

            throw new InvalidSubmissionException(
                    "A valid github.com repository URL is required."
            );
        }

        // 3. Duplicate Team Check
        if (repository.existsByTeamNameIgnoreCase(
                submission.getTeamName())) {

            throw new ConflictException(
                    "Team '" + submission.getTeamName() + 
                    "' has already submitted a project."
            );
        }

        // 4. Save Submission
        return repository.save(submission);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Submission> getAllSubmissions() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Submission getSubmission(@PathVariable Long id) {

        Optional<Submission> result = 
                repository.findById(id);

        if (result.isPresent()) {
            return result.get();
        }

        throw new ResourceNotFoundException(
                "Submission with ID " + id + " was not found."
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSubmission(@PathVariable Long id) {

        Optional<Submission> result = 
                repository.findById(id);

        if (result.isPresent()) {
            repository.delete(result.get());
        } else {
            throw new ResourceNotFoundException(
                    "Submission with ID " + id + " was not found."
            );
        }
    }
}
