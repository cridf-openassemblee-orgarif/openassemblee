package openassemblee.service;

import openassemblee.domain.Elu;
import openassemblee.domain.Mandat;
import openassemblee.repository.*;
import openassemblee.service.dto.MandatEditionDTO;
import org.elasticsearch.common.base.Strings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.Collection;

@Service
public class MandatService {

    @Inject
    private MandatRepository mandatRepository;

    @Inject
    private AppartenanceCommissionPermanenteRepository appartenanceCommissionPermanenteRepository;

    @Inject
    private AppartenanceGroupePolitiqueRepository appartenanceGroupePolitiqueRepository;

    @Inject
    private AppartenanceCommissionThematiqueRepository appartenanceCommissionThematiqueRepository;

    @Inject
    private FonctionCommissionPermanenteRepository fonctionCommissionPermanenteRepository;

    @Inject
    private FonctionExecutiveRepository fonctionExecutiveRepository;

    @Inject
    private FonctionCommissionThematiqueRepository fonctionCommissionThematiqueRepository;

    @Inject
    private FonctionGroupePolitiqueRepository fonctionGroupePolitiqueRepository;

    @Inject
    private EluService eluService;

    @Inject
    private EluRepository eluRepository;

    @Transactional
    public Mandat save(MandatEditionDTO dto) {
        Mandat result = mandatRepository.save(dto.getMandat());
        Boolean demission = Strings.isNullOrEmpty(dto.getMandat().getMotifDemission()) ||
            dto.getMandat().getDateDemission() != null;
        if (dto.getDemissionDiffusion() && demission) {
            Elu elu = eluRepository.findOne(dto.getMandat().getElu().getId());
            eluService.filterEluMandat(elu, dto.getMandat().getMandature());
            handle(appartenanceCommissionPermanenteRepository, elu.getAppartenancesCommissionPermanente(), dto.getMandat());
            handle(appartenanceCommissionThematiqueRepository, elu.getAppartenancesCommissionsThematiques(), dto.getMandat());
            handle(appartenanceGroupePolitiqueRepository, elu.getAppartenancesGroupePolitique(), dto.getMandat());
            handle(fonctionCommissionThematiqueRepository, elu.getFonctionsCommissionsThematiques(), dto.getMandat());
            handle(fonctionCommissionPermanenteRepository, elu.getFonctionsCommissionPermanente(), dto.getMandat());
            handle(fonctionGroupePolitiqueRepository, elu.getFonctionsGroupePolitique(), dto.getMandat());
            handle(fonctionExecutiveRepository, elu.getFonctionsExecutives(), dto.getMandat());
        }
        return result;
    }

    private <T> void handle(JpaRepository<T, Long> repo, Collection<T> collection, Mandat mandat) {
        collection.forEach(i -> {
            try {
                Method getDateFin = i.getClass().getMethod("getDateFin");
                LocalDate localDate = (LocalDate) getDateFin.invoke(i);
                Method getMotifFin = i.getClass().getMethod("getMotifFin");
                String motifDemission = (String) getMotifFin.invoke(i);
                if (localDate == null && Strings.isNullOrEmpty(motifDemission)) {
                    Method setDateFin = i.getClass().getMethod("setDateFin", LocalDate.class);
                    setDateFin.invoke(i, mandat.getDateDemission());
                    Method setMotifFin = i.getClass().getMethod("setMotifFin", String.class);
                    setMotifFin.invoke(i, mandat.getMotifDemission());
                    repo.save(i);
                }
            } catch (NoSuchMethodException
                | IllegalAccessException
                | InvocationTargetException
                | IllegalArgumentException e) {
                throw new RuntimeException("Impossible de diffusion la démission pour élu " + mandat.getElu().getId(), e);
            }
        });
    }
}
