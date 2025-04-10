package de.local.energycharts.infrastructure.adapter.mail;

import de.local.energycharts.mail.ports.out.MailSenderGateway;
import de.local.energycharts.mail.model.Mail;
import lombok.RequiredArgsConstructor;
import org.jmolecules.architecture.hexagonal.SecondaryAdapter;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@SecondaryAdapter
@Service
@RequiredArgsConstructor
public class JavaMailSenderGateway implements MailSenderGateway {

  private final JavaMailSender mailSender;
  private final SimpleMailMessage defaultMailMessage;

  /**
   * @param mail The mail to send.
   * @implNote The sender (from) of the mail must never be changed and must be the owner of the mail account,
   * otherwise the mail may be detected as spam. That's why 'reply to' is used, instead of 'from'.
   */
  public void sendMail(Mail mail) {
    var message = new SimpleMailMessage(defaultMailMessage);

    message.setReplyTo(mail.getFrom());
    if (mail.getTo() != null) {
      message.setTo(mail.getTo());
    }
    if (mail.getCc() != null) {
      message.setCc(mail.getCc());
    }
    message.setSubject(mail.getSubject());
    message.setText(mail.getMessage());

    mailSender.send(message);
  }
}
