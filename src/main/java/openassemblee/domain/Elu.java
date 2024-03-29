package openassemblee.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;
import javax.persistence.*;
import openassemblee.domain.enumeration.Civilite;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * A Elu.
 */
@Entity
@Table(name = "elu")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "elu")
public class Elu implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "civilite")
    private Civilite civilite;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "nom_jeune_fille")
    private String nomJeuneFille;

    @Column(name = "profession")
    private String profession;

    @Column(name = "date_naissance")
    private LocalDate dateNaissance;

    @Column(name = "lieu_naissance")
    private String lieuNaissance;

    @Column(name = "code_departement")
    private String codeDepartement;

    // FIXME rename departementElection
    @Column(name = "departement")
    private String departement;

    @Column(name = "image")
    private Long image;

    @Column(name = "import_uid")
    private String importUid;

    @Column(name = "liste_electorale")
    private String listeElectorale;

    @Column(name = "liste_court")
    private String listeCourt;

    @Column(name = "uid", columnDefinition = "varchar(255)")
    private String uid;

    @Column(name = "short_uid")
    private Long shortUid;

    // TODO mlo pourquoi tests et la pratique ne donne pas le meme resultat
    @OneToMany //(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    // TODO et sans la joinTable ?
    @JoinTable(
        name = "elu_adresses_postales",
        joinColumns = @JoinColumn(name = "elu_id"),
        inverseJoinColumns = @JoinColumn(name = "adresse_postale_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<AdressePostale> adressesPostales = new ArrayList<>();

    @OneToMany
    @JoinTable(
        name = "elu_numero_telephone",
        joinColumns = @JoinColumn(name = "elu_id"),
        inverseJoinColumns = @JoinColumn(name = "numero_telephone_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<NumeroTelephone> numerosTelephones = new ArrayList<>();

    @OneToMany
    @JoinTable(
        name = "elu_numero_fax",
        joinColumns = @JoinColumn(name = "elu_id"),
        inverseJoinColumns = @JoinColumn(name = "numero_fax_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<NumeroFax> numerosFax = new ArrayList<>();

    @OneToMany
    @JoinTable(
        name = "elu_adresse_mail",
        joinColumns = @JoinColumn(name = "elu_id"),
        inverseJoinColumns = @JoinColumn(name = "adresse_mail_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<AdresseMail> adressesMail = new ArrayList<>();

    @OneToMany
    @JoinTable(
        name = "elu_identite_internet",
        joinColumns = @JoinColumn(name = "elu_id"),
        inverseJoinColumns = @JoinColumn(name = "identite_internet_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<IdentiteInternet> identitesInternet = new ArrayList<>();

    @OneToMany(mappedBy = "elu")
    @Cache(usage = CacheConcurrencyStrategy.NONE)
    private List<FonctionExecutive> fonctionsExecutives = new ArrayList<>();

    @OneToMany(mappedBy = "elu")
    @Cache(usage = CacheConcurrencyStrategy.NONE)
    private List<FonctionCommissionPermanente> fonctionsCommissionPermanente =
        new ArrayList<>();

    @OneToMany(mappedBy = "elu")
    @Cache(usage = CacheConcurrencyStrategy.NONE)
    private List<AppartenanceCommissionPermanente> appartenancesCommissionPermanente =
        new ArrayList<>();

    @OneToMany(mappedBy = "elu")
    @Cache(usage = CacheConcurrencyStrategy.NONE)
    private List<AppartenanceGroupePolitique> appartenancesGroupePolitique =
        new ArrayList<>();

    @OneToMany(mappedBy = "elu")
    @Cache(usage = CacheConcurrencyStrategy.NONE)
    private List<FonctionGroupePolitique> fonctionsGroupePolitique =
        new ArrayList<>();

    @OneToMany(mappedBy = "elu")
    @Cache(usage = CacheConcurrencyStrategy.NONE)
    private Set<AppartenanceCommissionThematique> appartenancesCommissionsThematiques =
        new HashSet<>();

    @OneToMany(mappedBy = "elu")
    @Cache(usage = CacheConcurrencyStrategy.NONE)
    private Set<FonctionCommissionThematique> fonctionsCommissionsThematiques =
        new HashSet<>();

    @OneToMany(mappedBy = "elu")
    @Cache(usage = CacheConcurrencyStrategy.NONE)
    private Set<AppartenanceOrganisme> appartenancesOrganismes =
        new HashSet<>();

    @OneToMany(mappedBy = "elu")
    @Cache(usage = CacheConcurrencyStrategy.NONE)
    private Set<AutreMandat> autreMandats = new HashSet<>();

    @OneToMany(mappedBy = "elu")
    @Cache(usage = CacheConcurrencyStrategy.NONE)
    private Set<DistinctionHonorifique> distinctionHonorifiques =
        new HashSet<>();

    // TODO utile ici en fait ?
    @OneToMany(mappedBy = "elu")
    @Cache(usage = CacheConcurrencyStrategy.NONE)
    private Set<Mandat> mandats = new HashSet<>();

    public Elu() {}

    public Elu(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Civilite getCivilite() {
        return civilite;
    }

    public void setCivilite(Civilite civilite) {
        this.civilite = civilite;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNomJeuneFille() {
        return nomJeuneFille;
    }

    public void setNomJeuneFille(String nomJeuneFille) {
        this.nomJeuneFille = nomJeuneFille;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getLieuNaissance() {
        return lieuNaissance;
    }

    public void setLieuNaissance(String lieuNaissance) {
        this.lieuNaissance = lieuNaissance;
    }

    @Deprecated
    public String getCodeDepartement() {
        return codeDepartement;
    }

    public void setCodeDepartement(String codeDepartement) {
        this.codeDepartement = codeDepartement;
    }

    @Deprecated
    public String getDepartement() {
        return departement;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }

    public List<AdressePostale> getAdressesPostales() {
        return adressesPostales;
    }

    public void setAdressesPostales(List<AdressePostale> adressesPostales) {
        this.adressesPostales = adressesPostales;
    }

    public List<NumeroTelephone> getNumerosTelephones() {
        return numerosTelephones;
    }

    public void setNumerosTelephones(List<NumeroTelephone> numerosTelephones) {
        this.numerosTelephones = numerosTelephones;
    }

    public List<NumeroFax> getNumerosFax() {
        return numerosFax;
    }

    public void setNumerosFax(List<NumeroFax> numerosFax) {
        this.numerosFax = numerosFax;
    }

    public List<AdresseMail> getAdressesMail() {
        return adressesMail;
    }

    public void setAdressesMail(List<AdresseMail> adressesMail) {
        this.adressesMail = adressesMail;
    }

    public List<IdentiteInternet> getIdentitesInternet() {
        return identitesInternet;
    }

    public void setIdentitesInternet(List<IdentiteInternet> identitesInternet) {
        this.identitesInternet = identitesInternet;
    }

    public List<FonctionExecutive> getFonctionsExecutives() {
        return fonctionsExecutives;
    }

    public void setFonctionsExecutives(
        List<FonctionExecutive> fonctionsExecutives
    ) {
        this.fonctionsExecutives = fonctionsExecutives;
    }

    public List<FonctionCommissionPermanente> getFonctionsCommissionPermanente() {
        return fonctionsCommissionPermanente;
    }

    public void setFonctionsCommissionPermanente(
        List<FonctionCommissionPermanente> fonctionsCommissionPermanente
    ) {
        this.fonctionsCommissionPermanente = fonctionsCommissionPermanente;
    }

    public List<AppartenanceCommissionPermanente> getAppartenancesCommissionPermanente() {
        return appartenancesCommissionPermanente;
    }

    public void setAppartenancesCommissionPermanente(
        List<AppartenanceCommissionPermanente> appartenancesCommissionPermanente
    ) {
        this.appartenancesCommissionPermanente =
            appartenancesCommissionPermanente;
    }

    public List<AppartenanceGroupePolitique> getAppartenancesGroupePolitique() {
        return appartenancesGroupePolitique;
    }

    public void setAppartenancesGroupePolitique(
        List<AppartenanceGroupePolitique> appartenancesGroupePolitique
    ) {
        this.appartenancesGroupePolitique = appartenancesGroupePolitique;
    }

    public List<FonctionGroupePolitique> getFonctionsGroupePolitique() {
        return fonctionsGroupePolitique;
    }

    public void setFonctionsGroupePolitique(
        List<FonctionGroupePolitique> fonctionsGroupePolitique
    ) {
        this.fonctionsGroupePolitique = fonctionsGroupePolitique;
    }

    public Set<AppartenanceCommissionThematique> getAppartenancesCommissionsThematiques() {
        return appartenancesCommissionsThematiques;
    }

    public void setAppartenancesCommissionsThematiques(
        Set<AppartenanceCommissionThematique> appartenancesCommissionsThematiques
    ) {
        this.appartenancesCommissionsThematiques =
            appartenancesCommissionsThematiques;
    }

    public Set<FonctionCommissionThematique> getFonctionsCommissionsThematiques() {
        return fonctionsCommissionsThematiques;
    }

    public void setFonctionsCommissionsThematiques(
        Set<FonctionCommissionThematique> fonctionsCommissionsThematiques
    ) {
        this.fonctionsCommissionsThematiques = fonctionsCommissionsThematiques;
    }

    public Long getImage() {
        return image;
    }

    public void setImage(Long image) {
        this.image = image;
    }

    public Set<AppartenanceOrganisme> getAppartenancesOrganismes() {
        return appartenancesOrganismes;
    }

    public void setAppartenancesOrganismes(
        Set<AppartenanceOrganisme> appartenancesOrganismes
    ) {
        this.appartenancesOrganismes = appartenancesOrganismes;
    }

    public Set<AutreMandat> getAutreMandats() {
        return autreMandats;
    }

    public void setAutreMandats(Set<AutreMandat> autreMandats) {
        this.autreMandats = autreMandats;
    }

    public String getImportUid() {
        return importUid;
    }

    public String exportUid() {
        return "ELU" + id;
    }

    public void setImportUid(String importUid) {
        this.importUid = importUid;
    }

    public String getListeElectorale() {
        return listeElectorale;
    }

    public void setListeElectorale(String listeElectorale) {
        this.listeElectorale = listeElectorale;
    }

    public String getListeCourt() {
        return listeCourt;
    }

    public void setListeCourt(String listeCourt) {
        this.listeCourt = listeCourt;
    }

    public Set<DistinctionHonorifique> getDistinctionHonorifiques() {
        return distinctionHonorifiques;
    }

    public void setDistinctionHonorifiques(
        Set<DistinctionHonorifique> distinctionHonorifiques
    ) {
        this.distinctionHonorifiques = distinctionHonorifiques;
    }

    public Set<Mandat> getMandats() {
        return mandats;
    }

    public void setMandats(Set<Mandat> mandats) {
        this.mandats = mandats;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Long getShortUid() {
        return shortUid;
    }

    public void setShortUid(Long shortUid) {
        this.shortUid = shortUid;
    }

    public String civiliteComplete() {
        String c = civilite != null ? civilite.label() + " " : "";
        c += prenom != null ? prenom + " " : "";
        c += nom != null ? nom + " " : "";
        return c;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Elu elu = (Elu) o;
        return Objects.equals(id, elu.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return (
            "Elu{" +
            "id=" +
            id +
            ", civilite='" +
            civilite +
            "'" +
            ", nom='" +
            nom +
            "'" +
            ", prenom='" +
            prenom +
            "'" +
            ", nomJeuneFille='" +
            nomJeuneFille +
            "'" +
            '}'
        );
    }

    @JsonIgnore
    public String getCiviliteLabel() {
        return civilite != null ? civilite.label() : "Civilité non connue";
    }
}
