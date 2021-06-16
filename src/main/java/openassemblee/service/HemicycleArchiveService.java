package openassemblee.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import openassemblee.domain.HemicycleArchive;
import openassemblee.domain.HemicyclePlan;
import openassemblee.repository.HemicycleArchiveRepository;
import openassemblee.web.rest.dto.*;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.IOException;
import java.time.Instant;

import static openassemblee.config.Constants.parisZoneId;

@Service
public class HemicycleArchiveService {

    @Inject
    private HemicyclePlanService hemicyclePlanService;

    @Inject
    private HemicycleArchiveRepository hemicycleArchiveRepository;

    @Inject
    private HemicycleConfigurationRendererService hemicycleConfigurationRendererService;

    @Inject
    private ObjectMapper objectMapper;

    public HemicycleArchive save(HemicycleArchiveCreationDTO dto) {
        hemicyclePlanService.update(new HemicyclePlanUpdateDTO(dto.getPlanId(), dto.getData().getAssociations()));
        HemicycleArchive ha = new HemicycleArchive();
        ha.setDate(Instant.now().atZone(parisZoneId));
        ha.setSvgPlan(dto.getSvgPlan());
        HemicycleArchiveDataDTO data = new HemicycleArchiveDataDTO(dto.getData().getAssociations(),
            dto.getData().getElus(), dto.getData().getGroupePolitiques());
        String jsonData;
        try {
            jsonData = objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        ha.setJsonArchive(jsonData);
        HemicyclePlan hp = new HemicyclePlan();
        hp.setId(dto.getPlanId());
        ha.setHemicyclePlan(hp);
        HemicycleArchive result = hemicycleArchiveRepository.save(ha);
        return result;
    }

    public HemicycleArchiveDataWithConfigurationDTO get(Long id) {
        HemicycleArchive ha = hemicycleArchiveRepository.findOne(id);
        HemicycleArchiveDataDTO data ;
        try {
            data = objectMapper.readValue(ha.getJsonArchive(), HemicycleArchiveDataDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        HemicycleConfigurationDefinition d;
        try {
            d = objectMapper.readValue(ha.getHemicyclePlan().getConfiguration().getJsonConfiguration(),
                HemicycleConfigurationDefinition.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        HemicycleConfigurationRendu rendu = hemicycleConfigurationRendererService.hemicycle(d);

        return new HemicycleArchiveDataWithConfigurationDTO(data, rendu);
    }
}
