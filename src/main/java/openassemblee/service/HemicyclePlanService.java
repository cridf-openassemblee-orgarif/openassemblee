package openassemblee.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import openassemblee.domain.HemicycleConfiguration;
import openassemblee.domain.HemicyclePlan;
import openassemblee.domain.Seance;
import openassemblee.repository.HemicycleConfigurationRepository;
import openassemblee.repository.HemicyclePlanRepository;
import openassemblee.repository.search.HemicyclePlanSearchRepository;
import openassemblee.service.dto.EluListDTO;
import openassemblee.service.dto.HemicyclePlanCreationDTO;
import openassemblee.web.rest.dto.HemicycleConfigurationDefinition;
import openassemblee.web.rest.dto.HemicycleConfigurationRendu;
import openassemblee.web.rest.dto.HemicyclePlanAssociationsDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static openassemblee.config.Constants.parisZoneId;

@Service
public class HemicyclePlanService {

    @Inject
    private HemicycleConfigurationRepository hemicycleConfigurationRepository;

    @Inject
    private HemicyclePlanRepository hemicyclePlanRepository;

    @Inject
    private HemicyclePlanSearchRepository hemicyclePlanSearchRepository;

    @Inject
    private ObjectMapper objectMapper;

    @Inject
    private EluService eluService;

    @Inject
    private HemicycleConfigurationRendererService hemicycleConfigurationRendererService;

    public HemicyclePlan save(HemicyclePlanCreationDTO dto) {
        HemicyclePlan hd = new HemicyclePlan();
        hd.setLabel(dto.getLabel());
        HemicycleConfiguration hc = hemicycleConfigurationRepository.findOne(dto.getConfigurationId());
        hd.setConfiguration(hc);
        ZonedDateTime now = Instant.now().atZone(parisZoneId);
        hd.setCreationDate(now);
        hd.setLastModificationDate(now);
        String jsonPlan;
        if (dto.isFromAlphabeticOrder()) {
            HemicycleConfigurationDefinition d;
            try {
                d = objectMapper.readValue(hc.getJsonConfiguration(), HemicycleConfigurationDefinition.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Integer startChairNumber = d.frontChairs + 1;
            List<EluListDTO> elus = eluService.getAll(false, false, true);
            AtomicInteger index = new AtomicInteger(startChairNumber);
            List<HemicyclePlan.Association> associations = elus.stream()
                .sorted(Comparator.comparing(o -> StringUtils.stripAccents(o.getElu().getNom().toLowerCase())))
                .map(eluListDTO -> new HemicyclePlan.Association(index.getAndIncrement(), eluListDTO.getElu().getId()))
                .collect(Collectors.toList());
            try {
                jsonPlan = objectMapper.writeValueAsString(new HemicyclePlan.JsonPlan(associations));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        } else if (dto.getFromProjetId() != null) {
            HemicyclePlan projet = hemicyclePlanRepository.findOne(dto.getFromProjetId());
            jsonPlan = projet.getJsonPlan();
        } else if (dto.getFromSeanceId() != null) {
            Seance s = new Seance();
            s.setId(dto.getFromSeanceId());
            HemicyclePlan projet = hemicyclePlanRepository.findOneBySeance(s);
            jsonPlan = projet.getJsonPlan();
        } else {
            HemicyclePlan.JsonPlan p = new HemicyclePlan.JsonPlan(new ArrayList<>());
            try {
                jsonPlan = objectMapper.writeValueAsString(p);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            hd.setJsonPlan(jsonPlan);
        }
        hd.setJsonPlan(jsonPlan);
        return hemicyclePlanRepository.save(hd);
    }

    public HemicyclePlanAssociationsDTO get(Long id) {
        HemicyclePlan hp = hemicyclePlanRepository.findOne(id);
        HemicyclePlan.JsonPlan jsonPlan;
        try {
             jsonPlan = objectMapper.readValue(hp.getJsonPlan(), HemicyclePlan.JsonPlan.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        HemicycleConfigurationDefinition d;
        try {
            d = objectMapper.readValue(hp.getConfiguration().getJsonConfiguration(),
                HemicycleConfigurationDefinition.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        HemicycleConfigurationRendu rendu = hemicycleConfigurationRendererService.hemicycle(d);
        return new HemicyclePlanAssociationsDTO(jsonPlan.associations, rendu);
    }


}