package openassemblee.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import openassemblee.domain.AppartenanceCommissionThematique;
import openassemblee.domain.CommissionThematique;
import openassemblee.domain.FonctionCommissionThematique;
import openassemblee.repository.AdressePostaleRepository;
import openassemblee.repository.AppartenanceCommissionThematiqueRepository;
import openassemblee.repository.CommissionThematiqueRepository;
import openassemblee.repository.FonctionCommissionThematiqueRepository;
import openassemblee.repository.search.AdressePostaleSearchRepository;
import openassemblee.repository.search.CommissionThematiqueSearchRepository;
import openassemblee.service.dto.*;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommissionThematiqueService {

    @Inject
    private EluService eluService;

    @Inject
    private CommissionThematiqueRepository commissionThematiqueRepository;

    @Inject
    private CommissionThematiqueSearchRepository commissionThematiqueSearchRepository;

    @Inject
    private AdressePostaleRepository adressePostaleRepository;

    @Inject
    private AdressePostaleSearchRepository adressePostaleSearchRepository;

    @Inject
    private AppartenanceCommissionThematiqueRepository appartenanceCommissionThematiqueRepository;

    @Inject
    private FonctionCommissionThematiqueRepository fonctionCommissionThematiqueRepository;

    @Autowired
    private SessionMandatureService sessionMandatureService;

    @Transactional(readOnly = true)
    public List<CommissionThematique> getAll() {
        return commissionThematiqueRepository.findByMandature(
            sessionMandatureService.getMandature()
        );
    }

    @Transactional(readOnly = true)
    public List<CommissionThematiqueListDTO> getAllDtos() {
        List<CommissionThematique> list =
            commissionThematiqueRepository.findByMandature(
                sessionMandatureService.getMandature()
            );
        return list
            .stream()
            .map(gp -> {
                List<AppartenanceCommissionThematique> acts =
                    appartenanceCommissionThematiqueRepository.findAllByCommissionThematique(
                        gp
                    );
                int count = (int) acts
                    .stream()
                    .filter(CommissionThematiqueService::isAppartenanceCourante)
                    .count();
                return new CommissionThematiqueListDTO(gp, count);
            })
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CommissionThematiqueDTO get(Long id, Boolean loadGroupePolitiques) {
        CommissionThematique ct = commissionThematiqueRepository.findOne(id);
        if (ct == null) {
            return null;
        }
        List<AppartenanceCommissionThematiqueDTO> actDtos =
            appartenanceCommissionThematiqueRepository
                .findAllByCommissionThematique(ct)
                .stream()
                .map(a -> new AppartenanceCommissionThematiqueDTO(a, a.getElu())
                )
                .collect(Collectors.toList());
        List<FonctionCommissionThematiqueDTO> fctDtos =
            fonctionCommissionThematiqueRepository
                .findAllByCommissionThematique(ct)
                .stream()
                .map(a -> new FonctionCommissionThematiqueDTO(a, a.getElu()))
                .collect(Collectors.toList());
        if (loadGroupePolitiques) {
            actDtos.forEach(a -> {
                Hibernate.initialize(
                    a.getElu().getAppartenancesGroupePolitique()
                );
            });
            fctDtos.forEach(f -> {
                Hibernate.initialize(
                    f.getElu().getAppartenancesGroupePolitique()
                );
            });
        }
        return new CommissionThematiqueDTO(ct, actDtos, fctDtos);
    }

    // TODO va mériter un super test et une verif pour les dates
    // s'optimise ou... ?
    public static boolean isAppartenanceCourante(
        AppartenanceCommissionThematique a
    ) {
        // later remettre || a.getDateFin().isAfter(LocalDate.now());
        return a.getDateFin() == null;
    }

    public static boolean isFonctionCourante(FonctionCommissionThematique f) {
        // later remettre || a.getDateFin().isAfter(LocalDate.now());
        return f.getDateFin() == null;
    }

    @Transactional(readOnly = true)
    public ExcelExportService.Entry[] getExportEntries(Boolean filterAdresses) {
        List<ExcelExportService.Entry> entries = new ArrayList<>();
        List<CommissionThematique> cts =
            commissionThematiqueRepository.findByMandature(
                sessionMandatureService.getMandature()
            );
        List<List<String>> lines = new ArrayList<>();
        for (CommissionThematique ct : cts) {
            List<AppartenanceCommissionThematique> acts =
                appartenanceCommissionThematiqueRepository.findAllByCommissionThematique(
                    ct
                );
            int count = (int) acts
                .stream()
                .filter(CommissionThematiqueService::isAppartenanceCourante)
                .count();
            lines.add(Arrays.asList(ct.getNom(), count + " membres"));
        }
        entries.add(
            new ExcelExportService.Entry("Commission thématiques", lines)
        );
        cts.forEach(ct -> {
            List<List<String>> ctLines = new ArrayList<>();
            ctLines.add(Collections.singletonList("Fonctions"));
            List<FonctionCommissionThematique> fcts =
                fonctionCommissionThematiqueRepository
                    .findAllByCommissionThematique(ct)
                    .stream()
                    .filter(CommissionThematiqueService::isFonctionCourante)
                    //                .filter(f -> !removeDemissionaires || f.getDateFin() == null)
                    .collect(Collectors.toList());
            for (FonctionCommissionThematique f : fcts) {
                List<String> fLines = new ArrayList<>();
                fLines.add(f.getFonction());
                EluListDTO eld = eluService.eluToEluListDTO(
                    f.getElu(),
                    true,
                    filterAdresses
                );
                fLines.addAll(
                    eluService.xlsEluLine(
                        f.getElu(),
                        eld.getGroupePolitique(),
                        false
                    )
                );
                ctLines.add(fLines);
            }
            ctLines.add(Collections.singletonList("Appartenances"));
            List<AppartenanceCommissionThematique> acts =
                appartenanceCommissionThematiqueRepository
                    .findAllByCommissionThematique(ct)
                    .stream()
                    .filter(CommissionThematiqueService::isAppartenanceCourante)
                    //                .filter(a -> !removeDemissionaires || a.getDateFin() == null)
                    .collect(Collectors.toList());
            for (AppartenanceCommissionThematique a : acts) {
                List<String> aLines = new ArrayList<>();
                aLines.add("");
                EluListDTO eld = eluService.eluToEluListDTO(
                    a.getElu(),
                    true,
                    filterAdresses
                );
                aLines.addAll(
                    eluService.xlsEluLine(
                        a.getElu(),
                        eld.getGroupePolitique(),
                        false
                    )
                );
                ctLines.add(aLines);
            }
            entries.add(new ExcelExportService.Entry(ct.getNom(), ctLines));
        });
        return entries.toArray(new ExcelExportService.Entry[0]);
    }
}
