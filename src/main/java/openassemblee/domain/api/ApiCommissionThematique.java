package openassemblee.domain.api;

import java.time.LocalDate;

public class ApiCommissionThematique {

    public Long id;
    public String uid;
    //public Long shortUid;
    public String nom;
    public LocalDate dateDebut;
    public LocalDate dateFin;
    public String motifFin;
    public Long mandatureId;
}
