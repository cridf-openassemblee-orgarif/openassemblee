package openassemblee.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

import openassemblee.domain.enumeration.TypeIdentiteInternet;

/**
 * A IdentiteInternet.
 */
@Entity
@Table(name = "identite_internet")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "identiteinternet")
public class IdentiteInternet implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_identite_internet")
    private TypeIdentiteInternet typeIdentiteInternet;

    @Column(name = "url")
    private String url;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypeIdentiteInternet getTypeIdentiteInternet() {
        return typeIdentiteInternet;
    }

    public void setTypeIdentiteInternet(TypeIdentiteInternet typeIdentiteInternet) {
        this.typeIdentiteInternet = typeIdentiteInternet;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IdentiteInternet identiteInternet = (IdentiteInternet) o;
        return Objects.equals(id, identiteInternet.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "IdentiteInternet{" +
            "id=" + id +
            ", typeIdentiteInternet='" + typeIdentiteInternet + "'" +
            ", url='" + url + "'" +
            '}';
    }
}
