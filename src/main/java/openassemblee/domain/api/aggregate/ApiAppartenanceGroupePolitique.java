package openassemblee.domain.api.aggregate;

import java.time.LocalDate;
import java.util.List;

public class ApiAppartenanceGroupePolitique {

    public Long id;
    public Long groupePolitiqueId;
    public String nomGroupePolitique;
    public String nomCourtGroupePolitique;
    public LocalDate dateDebut;
    public LocalDate dateFin;
    public String motifFin;
    public Boolean actif;
    public List<ApiFonctionGroupePolitique> fonctions;
    public Long mandatureId;
}
