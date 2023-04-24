package openassemblee.domain.api;

import java.time.LocalDate;
import java.util.List;

public class ApiMandat {

    public Long id;
    public Long mandatureId;
    public Integer anneeDebutMandature;
    public Integer anneeFinMandature;
    public LocalDate dateDebutMandat;
    public String codeDepartement;
    public String departement;
    public LocalDate dateDemissionMandat;
    public String motifDemissionMandat;
    public Boolean actif;
    public List<ApiAppartenanceCommissionPermanente> appartenancesCommissionPermanente;
    public List<ApiFonctionExecutive> fonctionsExecutives;
    public List<ApiAutreMandat> autreMandats;
}
