package com.spring.mailservice.service;

//import com.carpool.auth.exeption.InternalServerErrorException;
import com.spring.mailservice.model.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import com.spring.mailservice.service.EmailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

@Service
public class EmailServiceImplementation implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine springTemplateEngine;

    @Autowired
    private TemplateEngine templateEngine;

    @Override
    public void sendSimpleEmail(Mail mail) {
        SimpleMailMessage msg = new SimpleMailMessage();

        msg.setFrom("kekovasudi@gmail.com");
        msg.setTo(mail.getMailTo());
        msg.setSubject(mail.getSubject());
        msg.setText(mail.getText());
        mailSender.send(msg);
    }

    @Override
    public void sendComplexEmail(Mail mail) throws SendFailedException {
        MimeMessage message = mailSender.createMimeMessage();

        try{
            MimeMessageHelper  helper = new MimeMessageHelper(message,MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

            Context context = new Context();

            context.setVariables(mail.getProps());

            String html = templateEngine.process("password_reset",context);

            helper.setTo(mail.getMailTo());
            helper.setText(html, true);
            helper.setSubject(mail.getSubject());
            helper.setFrom(mail.getFrom());

            mailSender.send(message);

        }catch(MessagingException ex){

        }
    }
}
