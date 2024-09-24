package fon.njt.mockfon.service;

import fon.njt.mockfon.exception.MailServiceException;
import fon.njt.mockfon.model.NotificationEmail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.util.Properties;

@Service
@Slf4j
public class MailService {

    @Autowired
    private JavaMailSender mailSender;
    private final String username;
    private final String password;
    private final MailContentBuilder mailContentBuilder;

    @Autowired
    public MailService(@Value("${spring.mail.username}") String username, @Value("${spring.mail.password}") String password, MailContentBuilder mailContentBuilder) {
        this.username = username;
        this.mailContentBuilder = mailContentBuilder;
        this.password = password;
    }

    @Async
    public void sendMail(NotificationEmail notificationEmail, String mailTemplate) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom("mockfon@gmail.com");
            helper.setTo(notificationEmail.getRecipient());
            helper.setSubject(notificationEmail.getSubject());
            helper.setText(mailContentBuilder.build(notificationEmail.getBody(), mailTemplate), true); // true za HTML sadržaj

            mailSender.send(mimeMessage);
            System.out.println("Done");

        } catch (MessagingException e) {
            e.printStackTrace();
            throw new MailServiceException("Exception occurred when sending email.");
        }
    }}
