package org.uatransport.service.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailContentBuilder emailContentBuilder;

    @Value("${spring.mail.username}")
    private String emailFrom;

    @Value("${subject.welcome.email}")
    private String welcomeSubject;

    @Value("${subject.password.confirmation}")
    private String changePasswordSubject;

    @Value("${subject.account.activation}")
    private String accountActivationSubject;

    public void prepareAndSendWelcomeEmail(String recipient, String message) throws MailException {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(emailFrom);
            messageHelper.setTo(recipient);
            messageHelper.setSubject(welcomeSubject);
            String content = emailContentBuilder.buildWelcomeHtml(message);
            messageHelper.setText(content, true);
        };
        try {
            mailSender.send(messagePreparator);
        } catch (MailException e) {
            System.out.println("Could not send email to : {} Error = {}" + recipient + e.getMessage());
        }
    }

    public void prepareAndSendConfirmPassEmail(String recipient, String userName, String message) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(emailFrom);
            messageHelper.setTo(recipient);
            messageHelper.setSubject(changePasswordSubject);
            String content = emailContentBuilder.buildConfirmPassHtml(userName, message);
            messageHelper.setText(content, true);
        };
        mailSender.send(messagePreparator);
    }

    public void prepareAndSendConfirmRegistrationEmail(String recipient, String userName, String message) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(emailFrom);
            messageHelper.setTo(recipient);
            messageHelper.setSubject(accountActivationSubject);
            String content = emailContentBuilder.buildConfirmRegistrationHtml(userName, message);
            messageHelper.setText(content, true);
        };
        mailSender.send(messagePreparator);
    }
}
