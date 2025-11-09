package com.example.eventfeedback.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import java.util.List;
import java.util.Map;

@Service
public class SentimentService {

    @Value("${huggingface.api.token}")
    private String apiToken;

    @Value("${huggingface.model}")
    private String model;

    private static final String API_URL = "https://router.huggingface.co/hf-inference/models/";

    public String analyzeSentiment(String text) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            headers.add("Accept", "application/json");
            headers.setBearerAuth(apiToken);

            Map<String, String> body = Map.of("inputs", text);
            HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

            ResponseEntity<List<List<Map<String, Object>>>> response = restTemplate.exchange(
                API_URL + model,
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<List<List<Map<String, Object>>>>() {}
            );

            if (response.getBody() == null || response.getBody().isEmpty() || 
                (response.getBody().get(0).size() == 1 && response.getBody().get(0).get(0).containsKey("error"))) {
                Thread.sleep(2000);
                response = restTemplate.exchange(
                    API_URL + model,
                    HttpMethod.POST,
                    request,
                    new ParameterizedTypeReference<List<List<Map<String, Object>>>>() {}
                );
            }

            if (response.getBody() != null && !response.getBody().isEmpty()) {
                List<Map<String, Object>> result = response.getBody().get(0);
                Map<String, Object> best = result.stream()
                        .max((a, b) -> Double.compare((Double) a.get("score"), (Double) b.get("score")))
                        .orElse(Map.of());

                String label = best.get("label").toString().toUpperCase();

                switch (label) {
                    case "POS":
                    case "POSITIVE":
                    case "LABEL_2":
                        return "POSITIVE";
                    case "NEG":
                    case "NEGATIVE":
                    case "LABEL_0":
                        return "NEGATIVE";
                    case "NEU":
                    case "NEUTRAL":
                    case "LABEL_1":
                    default:
                        return "NEUTRAL";
                }
            }
        } catch (Exception e) {
            System.err.println("⚠️ Sentiment API error: " + e.getMessage());
        }
        return "NEUTRAL";
    }
}