package de.local.energycharts.core.mail.ports.out;

import de.local.energycharts.core.mail.model.Mail;
import org.jmolecules.architecture.hexagonal.SecondaryPort;

@SecondaryPort
public interface MailSenderGateway {

    void sendMail(Mail mail);
}
