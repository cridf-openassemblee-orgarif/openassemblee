package openassemblee.domain.api;

import java.time.LocalDate;

public class ApiGroupePolitique {

    public Long id;
    public String uid;
    //public Long shortUid;
    public String nom;
    public String nomCourt;
    public ApiAdressePostale adressePostale;
    public LocalDate dateDebut;
    public LocalDate dateFin;
    public String motifFin;
    public Long image;
    public String website;
    public String phone;
    public String mail;
    public String fax;
    public Long mandatureId;
    public Boolean actif;
}
