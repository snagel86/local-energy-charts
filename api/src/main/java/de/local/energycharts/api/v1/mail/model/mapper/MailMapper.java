package de.local.energycharts.api.v1.mail.model.mapper;

import de.local.energycharts.api.v1.mail.model.MailRequest;
import de.local.energycharts.mail.model.Mail;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MailMapper {

  Mail map(MailRequest mailRequest);
}
