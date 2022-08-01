package de.local.energycharts.api.v1.contact.model.mapper;

import de.local.energycharts.api.v1.contact.model.ContactRequest;
import de.local.energycharts.api.v1.contact.model.ContactSuccessResponse;
import de.local.energycharts.mailing.contact.model.Contact;
import de.local.energycharts.mailing.contact.model.ContactSuccess;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContactMapper {

  Contact mapToBusinessModel(ContactRequest contactRequest);

  ContactSuccessResponse mapToResponse(ContactSuccess contactSuccess);
}
