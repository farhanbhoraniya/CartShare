package com.cmpe275.CartShare.async;

import com.cmpe275.CartShare.config.EmailConfiguration;
import com.cmpe275.CartShare.model.ConfirmationToken;
import com.cmpe275.CartShare.model.Order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class MailAsyncComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailAsyncComponent.class);

    private final EmailConfiguration emailConfiguration;

    @Autowired
    Environment env;
    
    @Autowired
    SpringTemplateEngine templateEngine;
    
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
        Properties props = mailSenderImpl.getJavaMailProperties();
        
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "false");
        return mailSenderImpl;
    }

    @Async("threadPoolTaskExecutor")
    public void sendMail(String email, ConfirmationToken confirmationToken) {
        LOGGER.info("Sending mail to " + email);
        String ip = InetAddress.getLoopbackAddress().getHostAddress();
        String port = env.getProperty("server.port");
        String confirmationURL = "http://" + ip + ":" + port + "/confirm-account?token=" + confirmationToken.getConfirmationtoken();
        String subject = "Verify Email";
        String body = "To confirm account click " + confirmationURL;

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText(body);

        getJavaMailSender().send(mailMessage);
    }
    
    @Async
    public void sendMessage(String email, String subject, String message) {
    	LOGGER.info("Sending mail to " + email);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        try {
        	getJavaMailSender().send(mailMessage);
        } catch(Exception e) {
        	System.out.println("Error");
        	System.out.println(e);
        }
        
        LOGGER.info("Mail sent");

    }
    
    @Async
    public void sendOrderMail(String to, String subject, String template, Map<String, Object> model) {
//    	String email = order.getBuyerid().getEmail();
//    	Map<String, Object> model = new HashMap<String, Object>();
//        model.put("status", order.getStatus());
//        model.put("orderItems", order.getOrderItems());
//        model.put("date", order.getDate());
//        model.put("id", order.getId());

    	LOGGER.info("Sending mail to " + to);

    	Context context = new Context();
        context.setVariables(model);
        
        String html = templateEngine.process(template, context);
        
        try {
        	MimeMessage message = getJavaMailSender().createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            helper.setTo(to);
	            helper.setText(html, true);
	            helper.setSubject(subject);
	            getJavaMailSender().send(message);
		       } catch (Exception e) {
		           System.out.println("Error while sending the email");
		           System.out.println(e);
		           return;
		       }
        
        LOGGER.info("Mail sent");

    }
}
