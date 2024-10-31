package de.local.energycharts.mail.ports.out;

import de.local.energycharts.mail.model.Mail;

public interface MailSenderGateway {

    void sendMail(Mail mail);
}
