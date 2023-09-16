package openassemblee.domain.api.aggregate;

import java.time.LocalDate;

public class ApiAutreMandat {

    public Long id;
    public String collectiviteOuOrganisme;
    public String fonction;
    // TODO preciser laquelle preferer peut-être
    public String dateDebutString;
    public LocalDate dateDebut;
    public LocalDate dateFin;
    public String motifFin;
    public Boolean actif;
}
