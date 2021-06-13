package openassemblee.domain;

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

    @Column(name = "website")
    private String website;

    @Column(name = "phone")
    private String phone;

    @Column(name = "mail")
    private String mail;

    @Column(name = "fax")
    private String fax;

    @Column(name = "import_uid")
    private String importUid;

    @Column(name = "couleur")
    private String couleur;

    @OneToMany(mappedBy = "groupePolitique")
    @Cache(usage = CacheConcurrencyStrategy.NONE)
    private List<AppartenanceGroupePolitique> appartenancesGroupePolitique = new ArrayList<>();

    @OneToMany(mappedBy = "groupePolitique")
    @Cache(usage = CacheConcurrencyStrategy.NONE)
    private List<FonctionGroupePolitique> fonctionsGroupePolitique = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "mandature_id")
    private Mandature mandature;

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

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getImportUid() {
        return importUid;
    }

    public String exportUid() {
        return "GP" + id;
    }

    public void setImportUid(String importUid) {
        this.importUid = importUid;
    }

    public String getCouleur() {
        return couleur;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
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

    public Mandature getMandature() {
        return mandature;
    }

    public void setMandature(Mandature mandature) {
        this.mandature = mandature;
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
