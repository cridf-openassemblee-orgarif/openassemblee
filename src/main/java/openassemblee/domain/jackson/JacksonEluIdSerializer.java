package openassemblee.domain.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import openassemblee.domain.Elu;

// TODO tester unit + integ
// FIXME remplaçable par un @JsonProperty(access = WRITE_ONLY) ?
public class JacksonEluIdSerializer extends JsonSerializer<Elu> {

    @Override
    public void serialize(
        Elu elu,
        JsonGenerator jsonGenerator,
        SerializerProvider serializerProvider
    ) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", elu.getId());
        jsonGenerator.writeEndObject();
    }
}
