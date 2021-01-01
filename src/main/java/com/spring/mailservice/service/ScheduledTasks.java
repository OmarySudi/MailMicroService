package com.spring.mailservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.mailservice.model.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.mail.SendFailedException;
import java.util.HashMap;
import java.util.Map;

@Component
public class ScheduledTasks {

    @Bean
    RestTemplate getRestTemplate(RestTemplateBuilder builder){
        return builder.build();
    }

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    EmailService emailService;

    @Scheduled(fixedDelay = 20000)
    public void sendEmailsAfterTenMinutes() throws JsonProcessingException {

        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8005/api/v1/calls/emails",String.class);

        if(response.getStatusCode().value() == 200){

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            JsonNode genralErrorCode = root.path("genralErrorCode");

            if(genralErrorCode.asInt() == 8000){

                JsonNode data = root.path("objects");

                for (JsonNode user:data) {

                    //System.out.println("Email is "+user.findValue("email"));
                    if(user.findValue("research_system_admin_role").asText().equals("ROLE_admin"))
                    {

                    }else{
                        Mail mail = new Mail();
                        mail.setMailTo(user.findValue("email").asText());
                        mail.setSubject("New calls");
                        mail.setFrom("kekovasudi@gmail.com");
                        //mail.setText("This email is for verification of email");

                        Map<String,Object> model = new HashMap<String,Object>();
                        model.put("link","http://localhost:8080/unsubscribe/"+user.findValue("user_id").asText());
                        mail.setProps(model);

                        try{
                            emailService.sendSubscriptionMail(mail);

                        }catch(SendFailedException ex){

                            System.out.println(ex.getMessage());
                        }
                    }

                }

                ResponseEntity<String> postresponse = restTemplate.postForEntity("http://localhost:8005/api/v1/calls/mark-email-sent",null,String.class);
            }
        }
    }
}
