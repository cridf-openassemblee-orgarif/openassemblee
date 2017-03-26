package fr.cridf.babylone14166.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A GroupePolitique.
 */
@Entity
@Table(name = "groupe_politique")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "groupepolitique")
public class GroupePolitique implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "nom_court")
    private String nomCourt;

    // for public WS only
    @Transient
    private String uuid;

    @OneToOne
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private AdressePostale adressePostale;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Column(name = "motif_fin")
    private String motifFin;

    @Column(name = "image")
    private Long image;

    @OneToMany(mappedBy = "groupePolitique")
    @Cache(usage = CacheConcurrencyStrategy.NONE)
    private List<AppartenanceGroupePolitique> appartenancesGroupePolitique = new ArrayList<>();

    @OneToMany(mappedBy = "groupePolitique")
    @Cache(usage = CacheConcurrencyStrategy.NONE)
    private List<FonctionGroupePolitique> fonctionsGroupePolitique = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNomCourt() {
        return nomCourt;
    }

    public void setNomCourt(String nomCourt) {
        this.nomCourt = nomCourt;
    }

    public AdressePostale getAdressePostale() {
        return adressePostale;
    }

    public void setAdressePostale(AdressePostale adressePostale) {
        this.adressePostale = adressePostale;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public String getMotifFin() {
        return motifFin;
    }

    public void setMotifFin(String motifFin) {
        this.motifFin = motifFin;
    }

    public Long getImage() {
        return image;
    }

    public void setImage(Long image) {
        this.image = image;
    }

    public List<AppartenanceGroupePolitique> getAppartenancesGroupePolitique() {
        return appartenancesGroupePolitique;
    }

    public void setAppartenancesGroupePolitique(List<AppartenanceGroupePolitique> appartenancesGroupePolitique) {
        this.appartenancesGroupePolitique = appartenancesGroupePolitique;
    }

    public List<FonctionGroupePolitique> getFonctionsGroupePolitique() {
        return fonctionsGroupePolitique;
    }

    public void setFonctionsGroupePolitique(List<FonctionGroupePolitique> fonctionsGroupePolitique) {
        this.fonctionsGroupePolitique = fonctionsGroupePolitique;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GroupePolitique groupePolitique = (GroupePolitique) o;
        return Objects.equals(id, groupePolitique.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "GroupePolitique{" +
            "id=" + id +
            ", nom='" + nom + "'" +
            ", nomCourt='" + nomCourt + "'" +
            ", dateDebut='" + dateDebut + "'" +
            ", dateFin='" + dateFin + "'" +
            ", motifFin='" + motifFin + "'" +
            '}';
    }
}
