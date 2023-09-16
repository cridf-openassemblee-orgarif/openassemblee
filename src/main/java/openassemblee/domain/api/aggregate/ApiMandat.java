package openassemblee.domain.api.aggregate;

import java.time.LocalDate;
import java.util.List;

public class ApiMandat {

    public Long id;
    public Long mandatureId;
    public Integer anneeDebutMandature;
    public Integer anneeFinMandature;
    public LocalDate dateDebutMandat;
    public Long listeElectoraleId;
    public String nomListeElectorale;
    public String nomCourtListeElectorale;
    public String codeDepartement;
    public String departement;
    public LocalDate dateDemissionMandat;
    public String motifDemissionMandat;
    public Boolean actif;
    public List<ApiAppartenanceCommissionPermanente> appartenancesCommissionPermanente;
    public List<ApiFonctionExecutive> fonctionsExecutives;
    public List<ApiFonctionCommissionPermanente> fonctions;
    public List<ApiAutreMandat> autreMandats;
}
