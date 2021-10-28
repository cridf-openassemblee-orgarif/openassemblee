package openassemblee.webservice;

import com.codahale.metrics.annotation.Timed;
import openassemblee.repository.*;
import openassemblee.service.SessionMandatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static openassemblee.service.EluService.isCurrentMandat;

@RestController
@RequestMapping("/api/publicdata/v1")
public class KentikaWebserviceResource {

    @Autowired
    private EluRepository eluRepository;

    @Autowired
    private SessionMandatureService sessionMandatureService;

    class ExportFonctionCP {
        public Long id;
        public String fonction;

        public ExportFonctionCP(Long id, String fonction) {
            this.id = id;
            this.fonction = fonction;
        }
    }

    class ExportFonctionExecutive {
        public Long id;
        public String fonction;

        public ExportFonctionExecutive(Long id, String fonction) {
            this.id = id;
            this.fonction = fonction;
        }
    }

    class ExportGroupePolitique {
        public Long id;
        public String nom;

        public ExportGroupePolitique(Long id, String nom) {
            this.id = id;
            this.nom = nom;
        }
    }

    class ExportFonctionGroupePolitique {
        public Long id;
        public String nom;
        public String fonction;

        public ExportFonctionGroupePolitique(Long id, String nom, String fonction) {
            this.id = id;
            this.nom = nom;
            this.fonction = fonction;
        }
    }

    class ExportCT {
        public Long id;
        public String nom;

        public ExportCT(Long id, String nom) {
            this.id = id;
            this.nom = nom;
        }
    }

    class ExportFonctionCT {
        public Long id;
        public String nom;
        public String fonction;

        public ExportFonctionCT(Long id, String nom, String fonction) {
            this.id = id;
            this.nom = nom;
            this.fonction = fonction;
        }
    }

    class ExportElu {
        public Long id;
        public String emailIleDeFrance;
        public String prenom;
        public String nom;
        public String emailDeCorrespondance;
        public Boolean appartenanceCp;
        public List<ExportFonctionCP> fonctionsCp;
        public List<ExportFonctionExecutive> fonctionsExecutive;
        public List<ExportGroupePolitique> appartenancesGroupePolitique;
        public List<ExportFonctionGroupePolitique> fonctionsGroupePolitique;
        public List<ExportCT> appartenancesCts;
        public List<ExportFonctionCT> fonctionsCts;

        public ExportElu(Long id, String emailIleDeFrance, String prenom, String nom, String emailDeCorrespondance, Boolean appartenanceCp, List<ExportFonctionCP> fonctionsCp, List<ExportFonctionExecutive> fonctionsExecutive, List<ExportGroupePolitique> appartenancesGroupePolitique, List<ExportFonctionGroupePolitique> fonctionsGroupePolitique, List<ExportCT> appartenancesCts, List<ExportFonctionCT> fonctionsCts) {
            this.id = id;
            this.emailIleDeFrance = emailIleDeFrance;
            this.prenom = prenom;
            this.nom = nom;
            this.emailDeCorrespondance = emailDeCorrespondance;
            this.appartenanceCp = appartenanceCp;
            this.fonctionsCp = fonctionsCp;
            this.fonctionsExecutive = fonctionsExecutive;
            this.appartenancesGroupePolitique = appartenancesGroupePolitique;
            this.fonctionsGroupePolitique = fonctionsGroupePolitique;
            this.appartenancesCts = appartenancesCts;
            this.fonctionsCts = fonctionsCts;
        }
    }

    @RequestMapping(value = "/kentika-export", method = RequestMethod.GET)
    @Timed
    @Transactional
    public ResponseEntity<List<ExportElu>> export() {
        List<ExportElu> elus = eluRepository.findAll().stream()
            .filter(e -> e.getDateDemission() == null)
            .filter(e -> isCurrentMandat(e.getMandats(), sessionMandatureService.getMandature()))
            .map(e ->
                new ExportElu(
                    e.getId(),
                    e.getAdressesMail().stream()
                        .filter(m -> m.getMail().endsWith("@iledefrance.fr"))
                        .findFirst().map(m -> m.getMail()).orElseGet(() -> ""),
                    e.getPrenom(),
                    e.getNom(),
                    e.getAdressesMail().stream()
                        .filter(m -> !m.getMail().trim().equals(""))
                        .findFirst().map(m -> m.getMail()).orElseGet(() -> ""),
                    e.getAppartenancesCommissionPermanente().stream()
                        .filter(a -> a.getDateFin() == null)
                        .collect(Collectors.toList()).size() != 0,
                    e.getFonctionsCommissionPermanente().stream()
                        .filter(a -> a.getDateFin() == null)
                        .filter(f -> f.getMandature().getId().equals(sessionMandatureService.getMandature().getId()))
                        .map(a ->
                            new ExportFonctionCP(a.getId(), a.getFonction())
                        ).collect(Collectors.toList()),
                    e.getFonctionsExecutives().stream()
                        .filter(a -> a.getDateFin() == null)
                        .filter(f -> f.getMandature().getId().equals(sessionMandatureService.getMandature().getId()))
                        .map(a ->
                            new ExportFonctionExecutive(a.getId(), a.getFonction())
                        ).collect(Collectors.toList()),
                    e.getAppartenancesGroupePolitique().stream()
                        .filter(a -> a.getDateFin() == null)
                        .filter(a -> a.getGroupePolitique().getMandature().getId().equals(sessionMandatureService.getMandature().getId()))
                        .map(a ->
                            new ExportGroupePolitique(a.getId(), a.getGroupePolitique().getNom())
                        ).collect(Collectors.toList()),
                    e.getFonctionsGroupePolitique().stream()
                        .filter(a -> a.getDateFin() == null)
                        .filter(f -> f.getGroupePolitique().getMandature().getId().equals(sessionMandatureService.getMandature().getId()))
                        .map(a ->
                            new ExportFonctionGroupePolitique(a.getId(), a.getGroupePolitique().getNom(), a.getFonction())
                        ).collect(Collectors.toList()),
                    e.getAppartenancesCommissionsThematiques().stream()
                        .filter(a -> a.getDateFin() == null)
                        .filter(a -> a.getCommissionThematique() != null)
                        .filter(a -> a.getCommissionThematique().getMandature().getId().equals(sessionMandatureService.getMandature().getId()))
                        .map(a ->
                            new ExportCT(a.getId(), a.getCommissionThematique().getNom())
                        ).collect(Collectors.toList()),
                    e.getFonctionsCommissionsThematiques().stream()
                        .filter(a -> a.getDateFin() == null)
                        .filter(a -> a.getCommissionThematique() != null)
                        .filter(a -> a.getCommissionThematique().getMandature().getId().equals(sessionMandatureService.getMandature().getId()))
                        .map(a ->
                            new ExportFonctionCT(a.getId(), a.getCommissionThematique().getNom(), a.getFonction())
                        ).collect(Collectors.toList())
                )
            ).collect(Collectors.toList());
        return ResponseEntity.ok(elus);
    }
}
