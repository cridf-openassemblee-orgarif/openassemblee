package openassemblee.config;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.*;
import openassemblee.domain.util.JSR310DateTimeSerializer;
import openassemblee.domain.util.JSR310LocalDateDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class JacksonConfiguration {

    @Bean
    Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
        JavaTimeModule module = new JavaTimeModule();
        module.addSerializer(
            OffsetDateTime.class,
            JSR310DateTimeSerializer.INSTANCE
        );
        module.addSerializer(
            ZonedDateTime.class,
            JSR310DateTimeSerializer.INSTANCE
        );
        module.addSerializer(
            LocalDateTime.class,
            JSR310DateTimeSerializer.INSTANCE
        );
        module.addSerializer(Instant.class, JSR310DateTimeSerializer.INSTANCE);
        module.addDeserializer(
            LocalDate.class,
            JSR310LocalDateDeserializer.INSTANCE
        );

        return new Jackson2ObjectMapperBuilder()
            .defaultViewInclusion(true)
            .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .findModulesViaServiceLoader(true)
            .modulesToInstall(module);
    }
}
