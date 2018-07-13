package org.uatransport.service.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailContentBuilder {
    private final TemplateEngine templateEngine;

    @Autowired
    public EmailContentBuilder(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    String buildWelcomeHtml(String firstName) {
        Context context = new Context();
        context.setVariable("firstName", firstName);
        return templateEngine.process("welcomeEmail.html", context);
    }

    public String buildConfirmPassHtml(String firstName, String confirmUrl) {
        Context context = new Context();
        context.setVariable("firstName", firstName);
        context.setVariable("confirmUrl", confirmUrl);
        return templateEngine.process("confirmPasswordEmail.html", context);
    }


    public String buildConfirmRegistrationHtml(String firstName, String confirmUrl) {
        Context context = new Context();
        context.setVariable("firstName", firstName);
        context.setVariable("confirmUrl", confirmUrl);
        return templateEngine.process("confirmRegistrationEmail.html", context);
    }

    public String buildFriendInvitationHtml(String userName, String friendName, String invitationLink) {
        Context context = new Context();

        context.setVariable("userName", userName);
        context.setVariable("friendName", friendName);
        context.setVariable("invitationLink", invitationLink);

        return templateEngine.process("inviteFriendEmail.html", context);
    }
}
