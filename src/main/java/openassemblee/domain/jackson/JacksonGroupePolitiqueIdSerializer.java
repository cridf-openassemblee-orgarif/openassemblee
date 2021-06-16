package openassemblee.domain.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import openassemblee.domain.GroupePolitique;

import java.io.IOException;

// TODO tester unit + integ
// FIXME remplaçable par un @JsonProperty(access = WRITE_ONLY) ?
public class JacksonGroupePolitiqueIdSerializer extends JsonSerializer<GroupePolitique> {

    @Override
    public void serialize(GroupePolitique groupePolitique, JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", groupePolitique.getId());
        jsonGenerator.writeEndObject();
    }
}
