package fr.cridf.babylone14166.domain.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import fr.cridf.babylone14166.domain.Elu;

// TODO tester unit + integ
public class JacksonEluIdSerializer extends JsonSerializer<Elu> {

    @Override
    public void serialize(Elu elu, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
        throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", elu.getId());
        jsonGenerator.writeEndObject();
    }
}
