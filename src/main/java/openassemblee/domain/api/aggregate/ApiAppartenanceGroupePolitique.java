package openassemblee.domain.api.aggregate;

import java.time.LocalDate;
import java.util.List;

public class ApiAppartenanceGroupePolitique {

    public Long id;
    // TODO api faire un sous objet ? + aussi pr fcts, cts...
    public Long groupePolitiqueId;
    // TODO say in API sont des shortcuts (ou pas)
    public String nomGroupePolitique;
    public String nomCourtGroupePolitique;
    public LocalDate dateDebut;
    public LocalDate dateFin;
    public String motifFin;
    public Boolean actif;
    public List<ApiFonctionGroupePolitique> fonctions;
    public Long mandatureId;
}
