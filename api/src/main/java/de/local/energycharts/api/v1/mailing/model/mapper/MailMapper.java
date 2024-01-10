package de.local.energycharts.api.v1.mailing.model.mapper;

import de.local.energycharts.api.v1.mailing.model.MailRequest;
import de.local.energycharts.mailing.model.Mail;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MailMapper {

  Mail map(MailRequest mailRequest);
}
