package com.email.writer.app;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;


import java.util.List;
import java.util.Map;

@Service
public class EmailGeneratorService {

    private final WebClient webclient;


    @Value("${gemini.api.url}")
    private String geminiApiUrl;
    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public EmailGeneratorService(WebClient.Builder webclientBuilder) {
        this.webclient = webclientBuilder.build();
    }

    public String generateEmailReply(EmailRequest emailRequest) {

        //built prompt used
        String prompt = builtPrompt(emailRequest);

        //craft request
        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of(
                                "parts", List.of(
                                        Map.of("text", prompt)
                                )
                        )
                )
        );


        //DO REQ AND GET RES
        String response = webclient.post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("generativelanguage.googleapis.com")
                        .path("/v1beta/models/gemini-2.5-flash:generateContent")
                        .queryParam("key", geminiApiKey)
                        .build())
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
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

            JsonNode candidates = rootNode.path("candidates");
            if (!candidates.isArray() || candidates.size() == 0) {
                return "No reply generated. Full response: " + response;
            }
            return  candidates
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();
        }
        catch (Exception e){
            e.printStackTrace();
            return "Error processing request"+ e.getMessage();
        }
    }

    private String builtPrompt(EmailRequest emailRequest) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Generate a professional email reply for the following email content.please don't generate a subject line");
        if (emailRequest.getTone() != null && !emailRequest.getTone().isEmpty()) {
            prompt.append("use a ").append(emailRequest.getTone()).append("tone!!");
        }
        prompt.append("\noriginal email: \n").append(emailRequest.getEmailContent());
        return prompt.toString();
    }
}
