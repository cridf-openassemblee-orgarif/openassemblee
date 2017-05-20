package openassemblee.service;

import openassemblee.domain.AppartenanceCommissionPermanente;
import openassemblee.domain.Elu;
import openassemblee.domain.FonctionCommissionPermanente;
import openassemblee.domain.FonctionExecutive;
import openassemblee.repository.AppartenanceCommissionPermanenteRepository;
import openassemblee.repository.EluRepository;
import openassemblee.repository.FonctionCommissionPermanenteRepository;
import openassemblee.repository.FonctionExecutiveRepository;
import openassemblee.service.dto.CommissionPermanenteDTO;
import openassemblee.service.dto.EluListDTO;
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
    private EluService eluService;

    @Transactional(readOnly = true)
    public CommissionPermanenteDTO getCommissionPermanente() {
        List<AppartenanceCommissionPermanente> acp = appartenanceCommissionPermanenteRepository.findAll().stream()
            .filter(CommissionPermanenteService::isAppartenanceCourante)
            .collect(Collectors.toList());
        List<FonctionCommissionPermanente> fcp = fonctionCommissionPermanenteRepository.findAll().stream()
            .filter(CommissionPermanenteService::isFonctionCourante)
            .collect(Collectors.toList());
        List<FonctionExecutive> fe = fonctionExecutiveRepository.findAll().stream()
            .filter(CommissionPermanenteService::isFonctionExecutiveCourante)
            .collect(Collectors.toList());
        Set<Long> elusIds = new HashSet<>();
        elusIds.addAll(acp.stream().map(f -> f.getElu().getId()).collect(Collectors.toList()));
        elusIds.addAll(fcp.stream().map(f -> f.getElu().getId()).collect(Collectors.toList()));
        elusIds.addAll(fe.stream().map(f -> f.getElu().getId()).collect(Collectors.toList()));
        Map<Long, Elu> elus = eluRepository.findAll(elusIds).stream().collect(Collectors.toMap(Elu::getId,
            Function.identity()));
        return new CommissionPermanenteDTO(acp, fcp, fe, elus);
    }

    private List<List<String>> getFonctionExecutivesLines(List<FonctionExecutive> fes) {
        List<List<String>> lines = new ArrayList<>();
        lines.add(Arrays.asList("Fonction éxécutive", "Civilité", "Prénom", "Nom", "Groupe politique", "Profession", "Lieu de naissance",
            "Date de naissance", "Date de début", "Date de fin", "Motif de fin"));
        for (FonctionExecutive fe : fes) {
            List<String> line = new ArrayList<>();
            line.add(fe.getFonction());
            line.addAll(getEluLine(fe.getElu()));
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
            line.addAll(getEluLine(acp.getElu()));
            lines.add(line);
            String dateDebut = acp.getDateDebut() != null ?
                acp.getDateDebut().format(DateTimeFormatter.ISO_LOCAL_DATE) : "Date de début inconnue";
            String dateFin = acp.getDateDebut() != null ?
                acp.getDateDebut().format(DateTimeFormatter.ISO_LOCAL_DATE) : "Date de fin inconnue";
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
            line.addAll(getEluLine(fcp.getElu()));
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

    private List<String> getEluLine(Elu elu) {
        String civilite = elu.getCivilite() != null ? elu.getCivilite().label() : "Civilité non connue";
        EluListDTO dto = eluService.eluToEluListDTO(elu);
        String groupePolitique = dto.getGroupePolitique() != null ? dto.getGroupePolitique().getNom() :
            "Aucun groupe politique";
        String dateNaissance = elu.getDateNaissance() != null ?
            elu.getDateNaissance().format(DateTimeFormatter.ISO_LOCAL_DATE) : "Date de naissance inconnue";
        return Arrays.asList(civilite, elu.getPrenom(), elu.getNom(), groupePolitique, elu.getProfession(),
            elu.getLieuNaissance(), dateNaissance);
    }

    @Transactional(readOnly = true)
    public ExportService.Entry[] getExportEntries() {
        List<Elu> elus = eluRepository.findAll();

        List<FonctionExecutive> fes = elus.stream()
            .flatMap(e -> e.getFonctionsExecutives().stream()).collect(Collectors.toList());
        List<AppartenanceCommissionPermanente> acps = elus.stream()
            .flatMap(e -> e.getAppartenancesCommissionPermanente().stream()).collect(Collectors.toList());
        List<FonctionCommissionPermanente> fcps = elus.stream()
            .flatMap(e -> e.getFonctionsCommissionPermanente().stream()).collect(Collectors.toList());
        ExportService.Entry[] entries = new ExportService.Entry[]{
            new ExportService.Entry("Fonctions éxécutives", getFonctionExecutivesLines(fes)),
            new ExportService.Entry("Membres", getAppartenancesLines(acps)),
            new ExportService.Entry("Fonctions", getFonctionsLines(fcps))
        };
        return entries;
    }

    public static boolean isAppartenanceCourante(AppartenanceCommissionPermanente a) {
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
}
