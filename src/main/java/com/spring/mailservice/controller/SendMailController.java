package com.spring.mailservice.controller;

import com.spring.mailservice.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.spring.mailservice.model.Mail;

import javax.mail.SendFailedException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class SendMailController {

    @Autowired
    EmailService emailService;

    @PostMapping("/password/reset")
    public void sendPasswordResetMail(@RequestParam(name="email") String email,
                                      @RequestParam(name= "link") String link,
                                      @RequestParam(name = "from") String from){
        Mail mail = new Mail();
        mail.setMailTo(email);
        mail.setSubject("Password Reset");
        mail.setFrom(from);
        //mail.setText("This email is for verification of email");

        Map<String,Object> model = new HashMap<String,Object>();
        model.put("link",link);
        mail.setProps(model);

        try{
            emailService.sendComplexEmail(mail);

        }catch(SendFailedException ex){

            System.out.println(ex.getMessage());
        }
    }
}
