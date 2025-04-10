package de.local.energycharts.mail.ports.out;

import de.local.energycharts.mail.model.Mail;
import org.jmolecules.architecture.hexagonal.SecondaryPort;

@SecondaryPort
public interface MailSenderGateway {

    void sendMail(Mail mail);
}
