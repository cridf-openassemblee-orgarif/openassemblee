package openassemblee.service;

import openassemblee.domain.*;
import openassemblee.repository.AppartenanceCommissionPermanenteRepository;
import openassemblee.repository.EluRepository;
import openassemblee.repository.FonctionCommissionPermanenteRepository;
import openassemblee.repository.FonctionExecutiveRepository;
import openassemblee.service.dto.CommissionPermanenteDTO;
import openassemblee.service.dto.EluEnFonctionDTO;
import openassemblee.service.dto.EluListDTO;
import openassemblee.service.dto.ExecutifDTO;
import openassemblee.service.util.EluNomComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CommissionPermanenteService {

    @Autowired
    private EluRepository eluRepository;
    @Autowired
    private AppartenanceCommissionPermanenteRepository appartenanceCommissionPermanenteRepository;
    @Autowired
    private FonctionCommissionPermanenteRepository fonctionCommissionPermanenteRepository;
    @Autowired
    private FonctionExecutiveRepository fonctionExecutiveRepository;
    @Autowired
    private SessionMandatureService sessionMandatureService;
    @Autowired
    private EluService eluService;

    @Transactional(readOnly = true)
    public ExecutifDTO getExecutif(Boolean removeDemissionaires) {
        Mandature mandature = sessionMandatureService.getMandature();
        List<FonctionExecutive> fe = fonctionExecutiveRepository.findByMandature(mandature).stream()
            .filter(CommissionPermanenteService::isFonctionExecutiveCourante)
            .filter(f -> !removeDemissionaires || f.getDateFin() == null)
            .sorted(this::sortFonctionExecutives)
            .collect(Collectors.toList());
        Set<Elu> elusFeIds = fe.stream().map(FonctionExecutive::getElu).collect(Collectors.toSet());
        List<FonctionCommissionPermanente> fcp = fonctionCommissionPermanenteRepository.findByMandature(mandature).stream()
            .filter(CommissionPermanenteService::isFonctionCourante)
            .filter(f -> !elusFeIds.contains(f.getElu()))
            .filter(f -> !removeDemissionaires || f.getDateFin() == null)
            .sorted(EluNomComparator.comparing(FonctionCommissionPermanente::getElu))
            .collect(Collectors.toList());
        Set<Long> elusIds = new HashSet<>();
        elusIds.addAll(fe.stream().map(f -> f.getElu().getId()).collect(Collectors.toList()));
        elusIds.addAll(fcp.stream().map(f -> f.getElu().getId()).collect(Collectors.toList()));
        Map<Long, Elu> elus = eluRepository.findAll(elusIds).stream().collect(Collectors.toMap(Elu::getId,
            Function.identity()));
        return new ExecutifDTO(fcp, fe, elus);
    }

    @Transactional(readOnly = true)
    public CommissionPermanenteDTO getCommissionPermanente() {
        List<AppartenanceCommissionPermanente> acp = appartenanceCommissionPermanenteRepository
            .findByMandature(sessionMandatureService.getMandature()).stream()
            .filter(this::isAppartenanceCourante)
            .sorted(EluNomComparator.comparing(AppartenanceCommissionPermanente::getElu))
            .collect(Collectors.toList());
        Set<Long> elusIds = new HashSet<>();
        elusIds.addAll(acp.stream().map(f -> f.getElu().getId()).collect(Collectors.toList()));
        Map<Long, Elu> elus = eluRepository.findAll(elusIds).stream().collect(Collectors.toMap(Elu::getId,
            Function.identity()));
        return new CommissionPermanenteDTO(acp, elus);
    }

    @Transactional(readOnly = true)
    public List<EluEnFonctionDTO> getFonctionExecutivesCommissionPermanenteDtos(Boolean filterAdresses) {
        return fonctionExecutiveRepository.findByMandature(sessionMandatureService.getMandature()).stream()
            .filter(CommissionPermanenteService::isFonctionExecutiveCourante)
            .sorted(this::sortFonctionExecutives)
            .map(f -> {
                EluListDTO eluDTO = eluService.eluToEluListDTO(f.getElu(), true, filterAdresses);
                return new EluEnFonctionDTO(eluDTO.getElu(), eluDTO.getGroupePolitique(), f.getFonction());
            }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EluEnFonctionDTO> getAppartenancesCommissionPermanenteDtos(Boolean filterAdresses, Boolean removeDemissionaires) {
        return appartenanceCommissionPermanenteRepository.findByMandature(sessionMandatureService.getMandature())
            .stream()
            .filter(this::isAppartenanceCourante)
            .filter(a -> !removeDemissionaires || a.getDateFin() == null)
            .sorted(EluNomComparator.comparing(AppartenanceCommissionPermanente::getElu))
            .map(a -> {
                EluListDTO eluDTO = eluService.eluToEluListDTO(a.getElu(), true, filterAdresses);
                return new EluEnFonctionDTO(eluDTO.getElu(), eluDTO.getGroupePolitique(), null);
            }).collect(Collectors.toList());
    }

    private List<List<String>> getFonctionExecutivesLines(List<FonctionExecutive> fes) {
        List<List<String>> lines = new ArrayList<>();
        lines.add(Arrays.asList("Fonction éxécutive", "Civilité", "Prénom", "Nom", "Groupe politique", "Profession", "Lieu de naissance",
            "Date de naissance", "Date de début", "Date de fin", "Motif de fin"));
        for (FonctionExecutive fe : fes) {
            List<String> line = new ArrayList<>();
            line.add(fe.getFonction());
            line.addAll(getEluLine(fe.getElu(), false, false));
            String dateDebut = fe.getDateDebut() != null ?
                fe.getDateDebut().format(DateTimeFormatter.ISO_LOCAL_DATE) : "Date de début inconnue";
            String dateFin = fe.getDateDebut() != null ?
                fe.getDateDebut().format(DateTimeFormatter.ISO_LOCAL_DATE) : "Date de fin inconnue";
            line.add(dateDebut);
            line.add(dateFin);
            line.add(fe.getMotifFin());
            lines.add(line);
        }
        return lines;
    }

    private List<List<String>> getAppartenancesLines(List<AppartenanceCommissionPermanente> acps) {
        List<List<String>> lines = new ArrayList<>();
        lines.add(Arrays.asList("Civilité", "Prénom", "Nom", "Groupe politique", "Profession", "Lieu de naissance",
            "Date de naissance", "Date de début", "Date de fin", "Motif de fin"));
        for (AppartenanceCommissionPermanente acp : acps) {
            List<String> line = new ArrayList<>();
            line.addAll(getEluLine(acp.getElu(), false, false));
            lines.add(line);
            String dateDebut = acp.getDateDebut() != null ?
                acp.getDateDebut().format(DateTimeFormatter.ISO_LOCAL_DATE) : "Date de début inconnue";
            String dateFin = acp.getDateFin() != null ?
                acp.getDateFin().format(DateTimeFormatter.ISO_LOCAL_DATE) : null;
            line.add(dateDebut);
            line.add(dateFin);
            line.add(acp.getMotifFin());
        }
        return lines;
    }

    private List<List<String>> getFonctionsLines(List<FonctionCommissionPermanente> fcps) {
        List<List<String>> lines = new ArrayList<>();
        lines.add(Arrays.asList("Fonction", "Civilité", "Prénom", "Nom", "Groupe politique", "Profession", "Lieu de naissance",
            "Date de naissance", "Date de début", "Date de fin", "Motif de fin"));
        for (FonctionCommissionPermanente fcp : fcps) {
            List<String> line = new ArrayList<>();
            line.add(fcp.getFonction());
            line.addAll(getEluLine(fcp.getElu(), false, false));
            lines.add(line);
            String dateDebut = fcp.getDateDebut() != null ?
                fcp.getDateDebut().format(DateTimeFormatter.ISO_LOCAL_DATE) : "Date de début inconnue";
            String dateFin = fcp.getDateDebut() != null ?
                fcp.getDateDebut().format(DateTimeFormatter.ISO_LOCAL_DATE) : "Date de fin inconnue";
            line.add(dateDebut);
            line.add(dateFin);
            line.add(fcp.getMotifFin());
        }
        return lines;
    }

    private List<String> getEluLine(Elu elu, Boolean loadAdresses, Boolean filterAdresses) {
        String civilite = elu.getCiviliteLabel();
        EluListDTO dto = eluService.eluToEluListDTO(elu, loadAdresses, filterAdresses);
        String groupePolitique = dto.getGroupePolitique() != null ? dto.getGroupePolitique().getNom() :
            "Aucun groupe politique";
        String dateNaissance = elu.getDateNaissance() != null ?
            elu.getDateNaissance().format(DateTimeFormatter.ISO_LOCAL_DATE) : "Date de naissance inconnue";
        return Arrays.asList(civilite, elu.getPrenom(), elu.getNom(), groupePolitique, elu.getProfession(),
            elu.getLieuNaissance(), dateNaissance);
    }

    public ExcelExportService.Entry[] getExecutifExportEntries() {
        ExecutifDTO dto = getExecutif(true);
        ExcelExportService.Entry[] entries = new ExcelExportService.Entry[]{
            new ExcelExportService.Entry("Exécutif", getFonctionExecutivesLines(dto.getFonctionsExecutives())),
            new ExcelExportService.Entry("Fonctions", getFonctionsLines(dto.getFonctions())),
        };
        return entries;
    }

    @Transactional(readOnly = true)
    public ExcelExportService.Entry[] getCommissionPermanenteExportEntries() {
        List<Elu> elus = eluRepository.findAll();

        List<AppartenanceCommissionPermanente> acps = elus.stream()
            .flatMap(e -> e.getAppartenancesCommissionPermanente().stream())
            .filter(a -> a.getDateFin() == null
                && a.getMandature().getId().equals(sessionMandatureService.getMandature().getId()))
            .collect(Collectors.toList());
        ExcelExportService.Entry[] entries = new ExcelExportService.Entry[]{
            new ExcelExportService.Entry("Membres", getAppartenancesLines(acps)),
        };
        return entries;
    }

    public boolean isAppartenanceCourante(AppartenanceCommissionPermanente a) {
        // plus tard : || a.getDateFin().isAfter(LocalDate.now())
        return a.getDateFin() == null;
    }

    public static boolean isFonctionCourante(FonctionCommissionPermanente f) {
        // plus tard : || f.getDateFin().isAfter(LocalDate.now())
        return f.getDateFin() == null;
    }

    public static boolean isFonctionExecutiveCourante(FonctionExecutive f) {
        // plus tard : || f.getDateFin().isAfter(LocalDate.now())
        return f.getDateFin() == null;
    }

    public List<List<String>> getFonctionsEntry(List<EluListDTO> dtos) {
        List<FonctionCommissionPermanente> fcps = dtos.stream()
            .map(EluListDTO::getElu)
            .flatMap(e -> e.getFonctionsCommissionPermanente().stream()).collect(Collectors.toList());
        return getFonctionsLines(fcps);
    }

    public int sortFonctionExecutives(FonctionExecutive fe1, FonctionExecutive fe2) {
        return fonctionScore(fe1) - fonctionScore(fe2);
    }

    public int fonctionScore(FonctionExecutive fe) {
        if (fe.getFonction().startsWith("Président")) {
            return 0;
        } else {
            Scanner scanner = new Scanner(fe.getFonction()).useDelimiter("[^0-9]+");
            if (scanner.hasNextInt()) {
                return scanner.nextInt();
            } else {
                return 100;
            }
        }
    }
}
