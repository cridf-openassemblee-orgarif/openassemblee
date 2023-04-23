package openassemblee.web.rest.mapper;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import openassemblee.domain.AuditTrail;
import openassemblee.web.rest.dto.AuditTrailDTO;
import org.springframework.stereotype.Component;

@Component
public class AuditTrailMapper {

    private static DateTimeFormatter dateTimeFormatter =
        DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public AuditTrailDTO entityToDto(AuditTrail at) {
        return new AuditTrailDTO(
            at.getId(),
            at.getEntity(),
            at.getEntityId(),
            at.getParentEntity(),
            at.getParentEntityId(),
            at.getAction(),
            at.getUser(),
            formatDate(at.getDate()),
            at.getDetails(),
            at.getDto(),
            at.getReason()
        );
    }

    public AuditTrail dtoToEntity(AuditTrailDTO dto) {
        return new AuditTrail(
            dto.getAction(),
            dto.getDetails(),
            dto.getReason()
        );
    }

    public String formatDate(ZonedDateTime date) {
        return dateTimeFormatter.format(date);
    }
}
