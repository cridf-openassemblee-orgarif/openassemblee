package openassemblee.domain.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import openassemblee.domain.Elu;

import java.io.IOException;

// TODO tester unit + integ
// FIXME remplaçable par un @JsonProperty(access = WRITE_ONLY) ?
public class JacksonEluIdSerializer extends JsonSerializer<Elu> {

    @Override
    public void serialize(Elu elu, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
        throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", elu.getId());
        jsonGenerator.writeEndObject();
    }
}
