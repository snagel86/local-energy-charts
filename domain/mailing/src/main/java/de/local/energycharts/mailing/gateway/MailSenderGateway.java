package de.local.energycharts.mailing.gateway;

import de.local.energycharts.mailing.model.Mail;

public interface MailSenderGateway {

    void sendMail(Mail mail);
}
