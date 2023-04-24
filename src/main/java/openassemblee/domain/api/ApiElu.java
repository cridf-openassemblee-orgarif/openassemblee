package openassemblee.domain.api;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import openassemblee.domain.enumeration.Civilite;

public class ApiElu {

    public Long id;
    public Civilite civilite;
    public String nom;
    public String prenom;
    public String nomJeuneFille;
    public String profession;
    public LocalDate dateNaissance;
    public String lieuNaissance;
    public String codeDepartement;
    public String departement;
    public Long image;
    public String uid;
    public Long shortUid;
    public Boolean actif;
    // mandats
    public List<ApiMandat> mandats;
    // gps / cts
    public List<ApiAppartenanceGroupePolitique> appartenancesGroupePolitique;
    public List<ApiAppartenanceCommissionThematique> appartenancesCommissionsThematiques;
    // coordonnees
    public List<ApiAdressePostale> adressesPostales;
    public List<ApiNumeroTelephone> numerosTelephones;
    public List<String> numerosFax;
    public List<String> adressesMail;
    public List<ApiIdentiteInternet> identitesInternet;
    public List<ApiDistinctionHonorifique> distinctionHonorifiques;
}
