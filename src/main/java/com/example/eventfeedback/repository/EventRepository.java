package com.example.eventfeedback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.eventfeedback.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {}