package fr.cridf.babylone14166.web.rest.mapper;

import fr.cridf.babylone14166.domain.AuditTrail;
import fr.cridf.babylone14166.web.rest.dto.AuditTrailDTO;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class AuditTrailMapper {

    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public AuditTrailDTO entityToDto(AuditTrail at) {
        return new AuditTrailDTO(at.getId(),
            at.getEntity(),
            at.getEntityId(),
            at.getParentEntity(),
            at.getParentEntityId(),
            at.getAction(),
            at.getUser(),
            dateTimeFormatter.format(at.getDate()),
            at.getDetails(),
            at.getReason());
    }

    public AuditTrail dtoToEntity(AuditTrailDTO dto) {
        return new AuditTrail(dto.getAction(), dto.getDetails(), dto.getReason());
    }
}
