package openassemblee.domain.api;

import java.time.LocalDate;
import java.util.List;

public class ApiAppartenanceGroupePolitique {

    public Long id;
    public Long groupePolitiqueId;
    // TODO say in API sont des shortcuts (ou pas)
    public String nomGroupePolitique;
    public String nomCourtGroupePolitique;
    public LocalDate dateDebut;
    public LocalDate dateFin;
    public String motifFin;
    public Boolean actif;
    public List<ApiFonctionGroupePolitique> fonctions;
}
