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
        Elu elu = new Elu();
        // TODO garder le format pour elasticsearch
        //        elu.setId(Long.parseLong(jsonParser.readValueAsTree().get("id").asToken().asString()));
        elu.setId(Long.parseLong(jsonParser.getValueAsString()));
        return elu;
    }
}
