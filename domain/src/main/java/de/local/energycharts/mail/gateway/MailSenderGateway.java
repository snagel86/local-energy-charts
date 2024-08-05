package de.local.energycharts.mail.gateway;

import de.local.energycharts.mail.model.Mail;

public interface MailSenderGateway {

    void sendMail(Mail mail);
}
