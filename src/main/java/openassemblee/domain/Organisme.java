package openassemblee.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Organisme.
 */
@Entity
@Table(name = "organisme")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "organisme")
public class Organisme implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "code_rne")
    private String codeRNE;

    @Column(name = "sigle")
    private String sigle;

    @Column(name = "type")
    private String type;

    @Column(name = "secteur")
    private String secteur;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Column(name = "motif_fin")
    private String motifFin;

    @Column(name = "import_uid")
    private String importUid;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "fax")
    private String fax;

    @Column(name = "phonetique")
    private String phonetique;

    @Column(name = "departement")
    private String departement;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private String status;

    @OneToOne
    private AdressePostale adressePostale;

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

    public String getCodeRNE() {
        return codeRNE;
    }

    public void setCodeRNE(String codeRNE) {
        this.codeRNE = codeRNE;
    }

    public String getSigle() {
        return sigle;
    }

    public void setSigle(String sigle) {
        this.sigle = sigle;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSecteur() {
        return secteur;
    }

    public void setSecteur(String secteur) {
        this.secteur = secteur;
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

    public String getImportUid() {
        return importUid;
    }

    public void setImportUid(String importUid) {
        this.importUid = importUid;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getPhonetique() {
        return phonetique;
    }

    public void setPhonetique(String phonetique) {
        this.phonetique = phonetique;
    }

    public String getDepartement() {
        return departement;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public AdressePostale getAdressePostale() {
        return adressePostale;
    }

    public void setAdressePostale(AdressePostale AdressePostale) {
        this.adressePostale = AdressePostale;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Organisme organisme = (Organisme) o;
        return Objects.equals(id, organisme.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Organisme{" +
            "id=" + id +
            ", nom='" + nom + "'" +
            ", codeRNE='" + codeRNE + "'" +
            ", sigle='" + sigle + "'" +
            ", type='" + type + "'" +
            ", secteur='" + secteur + "'" +
            ", dateDebut='" + dateDebut + "'" +
            ", dateFin='" + dateFin + "'" +
            ", motifFin='" + motifFin + "'" +
            '}';
    }
}
