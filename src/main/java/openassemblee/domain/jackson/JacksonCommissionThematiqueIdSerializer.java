package openassemblee.domain.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import openassemblee.domain.CommissionThematique;

// FIXME remplaçable par un @JsonProperty(access = WRITE_ONLY) ?
public class JacksonCommissionThematiqueIdSerializer
    extends JsonSerializer<CommissionThematique> {

    @Override
    public void serialize(
        CommissionThematique commissionThematique,
        JsonGenerator jsonGenerator,
        SerializerProvider serializerProvider
    ) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", commissionThematique.getId());
        jsonGenerator.writeEndObject();
    }
}
