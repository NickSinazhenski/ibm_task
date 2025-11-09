package com.example.eventfeedback.controller;

import com.example.eventfeedback.model.Event;
import com.example.eventfeedback.model.Feedback;
import com.example.eventfeedback.repository.EventRepository;
import com.example.eventfeedback.repository.FeedbackRepository;
import com.example.eventfeedback.service.SentimentService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/events/{eventId}/feedback")
public class FeedbackController {

    private final FeedbackRepository feedbackRepository;
    private final EventRepository eventRepository;
    private final SentimentService sentimentService;

    public FeedbackController(FeedbackRepository feedbackRepository,
                              EventRepository eventRepository,
                              SentimentService sentimentService) {
        this.feedbackRepository = feedbackRepository;
        this.eventRepository = eventRepository;
        this.sentimentService = sentimentService;
    }

    @PostMapping
    public Feedback addFeedback(@PathVariable Long eventId, @RequestBody Feedback feedback) {
        Optional<Event> event = eventRepository.findById(eventId);
        if (event.isEmpty()) throw new RuntimeException("Event not found");

        feedback.setEvent(event.get());
        feedback.setTimestamp(LocalDateTime.now());

        String sentiment = sentimentService.analyzeSentiment(feedback.getText());
        feedback.setSentiment(sentiment);

        return feedbackRepository.save(feedback);
    }
}