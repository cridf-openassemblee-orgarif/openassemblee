package fr.cridf.babylone14166.domain.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import fr.cridf.babylone14166.domain.AppartenanceGroupePolitique;
import fr.cridf.babylone14166.domain.Elu;
import fr.cridf.babylone14166.domain.GroupePolitique;
import fr.cridf.babylone14166.service.GroupePolitiqueService;

import java.io.IOException;
import java.util.Optional;

// TODO tester unit + integ
public class JacksonEluLightSerializer extends JsonSerializer<Elu> {

    @Override
    public void serialize(Elu elu, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
        throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", elu.getId());
        jsonGenerator.writeStringField("nom", elu.getNom());
        jsonGenerator.writeStringField("prenom", elu.getPrenom());
        Optional<GroupePolitique> gp = elu.getAppartenancesGroupePolitique().stream()
            .filter(GroupePolitiqueService::isAppartenanceCourante)
            .map(AppartenanceGroupePolitique::getGroupePolitique)
            .findFirst();
        if (gp.isPresent()) {
            jsonGenerator.writeStringField("groupePolitique", gp.get().getNom());
        }
        jsonGenerator.writeEndObject();
    }
}
