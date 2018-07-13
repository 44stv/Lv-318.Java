package org.uatransport.service.email;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
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

    @Value("${subject.friend.invitation}")
    private String invitation;

    @Value("${subject.account.activation}")
    private String accountActivationSubject;

    public void prepareAndSendWelcomeEmail(String email, String firstName) throws MailException {
        ExecutorService emailExecutor = Executors.newSingleThreadExecutor();
        try {
            emailExecutor.execute(() -> {
                MimeMessagePreparator messagePreparator = mimeMessage -> {
                    MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
                    messageHelper.setFrom(emailFrom);
                    messageHelper.setTo(email);
                    messageHelper.setSubject(welcomeSubject);
                    String content = emailContentBuilder.buildWelcomeHtml(firstName);
                    messageHelper.setText(content, true);
                };
                mailSender.send(messagePreparator);
            });
        } finally {
            emailExecutor.shutdown();
        }
    }

    public void prepareAndSendConfirmPassEmail(String email, String firstName, String confirmUrl) {
        ExecutorService emailExecutor = Executors.newSingleThreadExecutor();
        try {
            emailExecutor.execute(() -> {
                MimeMessagePreparator messagePreparator = mimeMessage -> {
                    MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
                    messageHelper.setFrom(emailFrom);
                    messageHelper.setTo(email);
                    messageHelper.setSubject(changePasswordSubject);
                    String content = emailContentBuilder.buildConfirmPassHtml(firstName, confirmUrl);
                    messageHelper.setText(content, true);
                };
                mailSender.send(messagePreparator);
            });
        } finally {
            emailExecutor.shutdown();
        }
    }

    public void prepareAndSendConfirmRegistrationEmail(String email, String firstName, String confirmUrl) {
        ExecutorService emailExecutor = Executors.newSingleThreadExecutor();
        try {
            emailExecutor.execute(() -> {
                MimeMessagePreparator messagePreparator = mimeMessage -> {
                    MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
                    messageHelper.setFrom(emailFrom);
                    messageHelper.setTo(email);
                    messageHelper.setSubject(accountActivationSubject);
                    String content = emailContentBuilder.buildConfirmRegistrationHtml(firstName, confirmUrl);
                    messageHelper.setText(content, true);
                };
                mailSender.send(messagePreparator);
            });
        } finally {
            emailExecutor.shutdown();
        }
    }

    public void prepareAndSendFriendInvitationEmail(String friendEmail, String userName, String friendName,
            String invitationLink) {
        ExecutorService emailExecutor = Executors.newSingleThreadExecutor();
        try {
            emailExecutor.execute(() -> {
                MimeMessagePreparator messagePreparator = mimeMessage -> {
                    MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
                    messageHelper.setFrom(emailFrom);
                    messageHelper.setTo(friendEmail);
                    messageHelper.setSubject(invitation);
                    String content = emailContentBuilder.buildFriendInvitationHtml(userName, friendName,
                            invitationLink);
                    messageHelper.setText(content, true);
                };
                mailSender.send(messagePreparator);
            });
        } finally {
            emailExecutor.shutdown();
        }
    }

}
