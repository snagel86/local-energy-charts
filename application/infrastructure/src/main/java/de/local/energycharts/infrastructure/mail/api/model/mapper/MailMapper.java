package de.local.energycharts.infrastructure.mail.api.model.mapper;

import de.local.energycharts.infrastructure.mail.api.model.MailRequest;
import de.local.energycharts.core.mail.model.Mail;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MailMapper {

  Mail map(MailRequest mailRequest);
}
