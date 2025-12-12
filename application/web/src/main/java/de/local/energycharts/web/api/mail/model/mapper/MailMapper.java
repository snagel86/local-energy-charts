package de.local.energycharts.web.api.mail.model.mapper;

import de.local.energycharts.core.mail.model.Mail;
import de.local.energycharts.web.api.mail.model.MailRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MailMapper {

  Mail map(MailRequest mailRequest);
}
