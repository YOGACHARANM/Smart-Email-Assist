package com.email.writer.app;


import org.springframework.stereotype.Service;

@Service
public class EmailGeneratorService {

    public String generateEmailReply(EmailRequest emailRequest ){

        //built prompt used 
        String prompt =builtPrompt(emailRequest);





    }

    private String builtPrompt(EmailRequest emailRequest) {
            StringBuilder prompt=new StringBuilder();
            prompt.append("Generate a professional email reply for the following email content.please don't generate a subject line");
            if(emailRequest.getTone() !=null && !emailRequest.getTone().isEmpty()){
                prompt.append("use a ").append(emailRequest.getTone()).append("tone!!");
            }
            prompt.append("\noriginal email: \n").append(emailRequest.getEmailcontent()) ;
            return prompt.toString();
    }
}
