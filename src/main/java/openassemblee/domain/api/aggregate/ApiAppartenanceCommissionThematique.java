package openassemblee.domain.api.aggregate;

import java.time.LocalDate;
import java.util.List;

public class ApiAppartenanceCommissionThematique {

    public Long id;
    public Long commissionThematiqueId;
    public String nomCommissionThematique;
    public LocalDate dateDebut;
    public LocalDate dateFin;
    public String motifFin;
    public Boolean actif;
    public List<ApiFonctionCommissionThematique> fonctions;
    public Long mandatureId;
}
