package com.cmpe275.CartShare.async;

import com.cmpe275.CartShare.config.EmailConfiguration;
import com.cmpe275.CartShare.model.ConfirmationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

@Component
public class MailAsyncComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailAsyncComponent.class);

    private final EmailConfiguration emailConfiguration;

    @Autowired
    public MailAsyncComponent(EmailConfiguration emailConfiguration) {
        this.emailConfiguration = emailConfiguration;
    }

    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSenderImpl = new JavaMailSenderImpl();
        mailSenderImpl.setHost(emailConfiguration.getHost());
        mailSenderImpl.setPort(emailConfiguration.getPort());
        mailSenderImpl.setUsername(emailConfiguration.getUsername());
        mailSenderImpl.setPassword(emailConfiguration.getPassword());
        return mailSenderImpl;
    }

    @Async("threadPoolTaskExecutor")
    public void sendMail(String email, ConfirmationToken confirmationToken) {
        LOGGER.info("Sending mail to " + email);
        String ip = InetAddress.getLoopbackAddress().getHostAddress();
        String confirmationURL = "http://" + ip + ":9000/confirm-account?token=" + confirmationToken.getConfirmationtoken();
        String subject = "Verify Email";
        String body = "To confirm account click " + confirmationURL;

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText(body);

        getJavaMailSender().send(mailMessage);
    }
}
