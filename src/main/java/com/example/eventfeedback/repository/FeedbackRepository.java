package com.example.eventfeedback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.eventfeedback.model.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {}