package fr.cridf.babylone14166.web.rest.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import fr.cridf.babylone14166.domain.Elu;

public class JacksonEluDeserializer extends JsonDeserializer<Elu> {

    @Override
    public Elu deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
        throws IOException, JsonProcessingException {
        return new Elu(Long.parseLong(jsonParser.getValueAsString()));
    }
}
