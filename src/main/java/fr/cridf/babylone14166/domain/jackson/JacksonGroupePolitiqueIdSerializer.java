package fr.cridf.babylone14166.domain.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import fr.cridf.babylone14166.domain.GroupePolitique;

// TODO tester unit + integ
public class JacksonGroupePolitiqueIdSerializer extends JsonSerializer<GroupePolitique> {

    @Override
    public void serialize(GroupePolitique groupePolitique, JsonGenerator jsonGenerator,
        SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", groupePolitique.getId());
        jsonGenerator.writeEndObject();
    }
}
