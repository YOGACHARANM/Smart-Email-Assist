package com.email.writer.app;


import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

@Service
public class EmailGeneratorService {

    public EmailGeneratorService(WebClient.Builder webclient) {
        this.webclient = webclient.build();
    }

    private final WebClient webclient;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;
    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public String generateEmailReply(EmailRequest emailRequest) {

        //built prompt used 
        String prompt = builtPrompt(emailRequest);

        //craft request
        Map<String, Object> requestBody = Map.of(
                "contents", new Object[]{
                        Map.of("parts", new Object[]{
                                Map.of("text", prompt)
                        })}
        );
        //DO REQ AND GET RES
        String response= webclient.post()
                .uri(geminiApiUrl + geminiApiKey)
                .header("content-Type","application/json")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return extractResponseContent(response);
    }
// extract response and return
    private String extractResponseContent(String response) {
        try {
            ObjectMapper mapper =new ObjectMapper();
            JsonNode rootNode= mapper.readTree(response);
            return  rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();
        }
        catch (Exception e){
            return "Error processing request"+ e.getMessage();
        }
    }

    private String builtPrompt(EmailRequest emailRequest) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Generate a professional email reply for the following email content.please don't generate a subject line");
        if (emailRequest.getTone() != null && !emailRequest.getTone().isEmpty()) {
            prompt.append("use a ").append(emailRequest.getTone()).append("tone!!");
        }
        prompt.append("\noriginal email: \n").append(emailRequest.getEmailcontent());
        return prompt.toString();
    }
}
