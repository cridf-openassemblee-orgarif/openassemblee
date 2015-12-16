package fr.cridf.babylone14166.domain.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import fr.cridf.babylone14166.domain.CommissionThematique;

public class JacksonCommissionThematiqueIdSerializer extends JsonSerializer<CommissionThematique> {

    @Override
    public void serialize(CommissionThematique commissionThematique, JsonGenerator jsonGenerator,
        SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", commissionThematique.getId());
        jsonGenerator.writeEndObject();
    }

}
