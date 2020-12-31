package com.spring.mailservice.service;

import com.spring.mailservice.model.Mail;

import javax.mail.SendFailedException;

public interface EmailService {

    void sendSimpleEmail(Mail mail);

    void sendComplexEmail(Mail mail) throws SendFailedException;

    void sendSubscriptionMail(Mail mail) throws SendFailedException;
}
