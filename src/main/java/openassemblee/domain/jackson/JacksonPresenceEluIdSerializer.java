package openassemblee.domain.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import openassemblee.domain.PresenceElu;

import java.io.IOException;

// FIXME parce que @JsonProperty(access = WRITE_ONLY) ne marche pas du tout justement !
// documenter ?
public class JacksonPresenceEluIdSerializer extends JsonSerializer<PresenceElu> {

    @Override
    public void serialize(PresenceElu presenceElu, JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", presenceElu.getId());
        jsonGenerator.writeEndObject();
    }

}
