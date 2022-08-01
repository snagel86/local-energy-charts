package de.local.energycharts.mailing.config;

import de.local.energycharts.mailing.contact.service.ContactService;
import java.util.Properties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@ConfigurationProperties(prefix = "spring.mail")
@Data
public class MailingConfiguration {

  private String host;
  private Integer port;
  private String username;
  private String password;

  @Bean
  public JavaMailSender createJavaMailSender() {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setHost(host);
    mailSender.setPort(port);
    mailSender.setUsername(username);
    mailSender.setPassword(password);

    Properties props = mailSender.getJavaMailProperties();
    props.put("simplejavamail.defaults.to.address", username);
    props.put("mail.transport.protocol", "smtp");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.ssl.protocols", "TLSv1.2");

    return mailSender;
  }

  @Bean
  public SimpleMailMessage createDefaultSimpleMailMessage() {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(username);
    return message;
  }

  @Bean
  public ContactService createContactApiService(JavaMailSender javaMailSender){
    SimpleMailMessage defaultMailMessage = new SimpleMailMessage();
    defaultMailMessage.setTo(username);
    return new ContactService(javaMailSender, defaultMailMessage);
  }
}
