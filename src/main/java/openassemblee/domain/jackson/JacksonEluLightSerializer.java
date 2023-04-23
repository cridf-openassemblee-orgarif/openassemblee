package openassemblee.domain.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.util.Optional;
import openassemblee.domain.AppartenanceGroupePolitique;
import openassemblee.domain.Elu;
import openassemblee.domain.GroupePolitique;
import openassemblee.service.GroupePolitiqueService;

// TODO tester unit + integ
public class JacksonEluLightSerializer extends JsonSerializer<Elu> {

    @Override
    public void serialize(
        Elu elu,
        JsonGenerator jsonGenerator,
        SerializerProvider serializerProvider
    ) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", elu.getId());
        jsonGenerator.writeStringField("nom", elu.getNom());
        jsonGenerator.writeStringField("prenom", elu.getPrenom());
        Optional<GroupePolitique> gp = elu
            .getAppartenancesGroupePolitique()
            .stream()
            .filter(GroupePolitiqueService::isAppartenanceCourante)
            .map(AppartenanceGroupePolitique::getGroupePolitique)
            .findFirst();
        if (gp.isPresent()) {
            jsonGenerator.writeStringField(
                "groupePolitique",
                gp.get().getNomCourt()
            );
        }
        jsonGenerator.writeEndObject();
    }
}
