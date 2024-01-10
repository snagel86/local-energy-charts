package de.local.energycharts.infrastructure.mailing.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@ConfigurationProperties(prefix = "spring.mail")
@Data
public class MailingConfiguration {

    private String host;
    private Integer port;
    private String username;
    private String password;

    @Bean
    public JavaMailSender createMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        return mailSender;
    }

    @Bean
    public SimpleMailMessage createDefaultMailMessage(
    ) {
        SimpleMailMessage defaultMailMessage = new SimpleMailMessage();
        // The sender (from) of the mail must never be changed and must be the owner of the mail account,
        // otherwise the mail may be detected as spam.
        // If replying to a person other than the sender, replyTo must be used.
        defaultMailMessage.setFrom(username);
        defaultMailMessage.setReplyTo(username);
        defaultMailMessage.setTo(username);
        return defaultMailMessage;
    }
}
