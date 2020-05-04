package com.cmpe275.CartShare.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl {

	@Autowired
	JavaMailSender emailSender;
	
	public void sendEmail(String to, String subject, String text) {
		
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);
//		emailSender.
		emailSender.send(message);
	}
}
