package com.example.eventfeedback.service;

import org.springframework.stereotype.Service;
import com.example.eventfeedback.model.*;
import com.example.eventfeedback.repository.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FeedbackService {
    private final FeedbackRepository feedbackRepo;
    private final EventRepository eventRepo;
    private final SentimentService sentimentService;

    public FeedbackService(FeedbackRepository feedbackRepo, EventRepository eventRepo, SentimentService sentimentService) {
        this.feedbackRepo = feedbackRepo;
        this.eventRepo = eventRepo;
        this.sentimentService = sentimentService;
    }

    public Feedback addFeedback(Long eventId, String text) {
        Event event = eventRepo.findById(eventId).orElseThrow();
       String sentiment = sentimentService.analyzeSentiment(text);

        Feedback feedback = new Feedback();
        feedback.setText(text);
        feedback.setSentiment(sentiment);
        feedback.setEvent(event);

        return feedbackRepo.save(feedback);
    }

    public Map<String, Object> getSummary(Long eventId) {
        List<Feedback> feedbacks = feedbackRepo.findAll().stream()
                .filter(f -> f.getEvent().getId().equals(eventId))
                .collect(Collectors.toList());

        long pos = feedbacks.stream().filter(f -> f.getSentiment().contains("pos")).count();
        long neu = feedbacks.stream().filter(f -> f.getSentiment().contains("neu")).count();
        long neg = feedbacks.stream().filter(f -> f.getSentiment().contains("neg")).count();

        return Map.of(
            "eventId", eventId,
            "totalFeedbacks", feedbacks.size(),
            "positive", pos,
            "neutral", neu,
            "negative", neg
        );
    }
}