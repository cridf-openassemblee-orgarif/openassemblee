package openassemblee.service;

import openassemblee.domain.HemicycleConfiguration;
import openassemblee.repository.HemicycleConfigurationRepository;
import org.elasticsearch.common.io.Streams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.IOException;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static openassemblee.config.Constants.parisZoneId;

@Service
public class HemicycleConfigurationService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static String firstConfigurationLabel = "Mai 2020";

    @Inject
    private HemicycleConfigurationRepository hemicycleConfigurationRepository;

    public List<HemicycleConfiguration> getAllSorted() {
        List<HemicycleConfiguration> list = hemicycleConfigurationRepository.findAll();
        if (list.isEmpty()) {
            logger.info("Injection de la première configuration d'hémicycle \"" + firstConfigurationLabel + "\"");
            String json;
            try {
                json = new String(Streams.copyToByteArray(getClass().getClassLoader()
                    .getResourceAsStream("hemicycle/hemicycle-configuration-2020.json")));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            HemicycleConfiguration hc = new HemicycleConfiguration();
            hc.setFrozen(true);
            hc.setFrozenDate(Instant.now().atZone(parisZoneId));
            hc.setJsonConfiguration(json);
            hc.setLabel(firstConfigurationLabel);
            hemicycleConfigurationRepository.save(hc);
            list = hemicycleConfigurationRepository.findAll();
        }
        return list
            .stream()
            .sorted(Comparator.comparing(HemicycleConfiguration::getCreationDate).reversed())
            .collect(Collectors.toList());
    }

}
