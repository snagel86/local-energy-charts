package de.local.energycharts.mail.port;

import de.local.energycharts.mail.model.Mail;

public interface MailSenderGateway {

    void sendMail(Mail mail);
}
