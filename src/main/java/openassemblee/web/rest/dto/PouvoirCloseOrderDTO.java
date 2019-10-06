package openassemblee.web.rest.dto;

import java.time.LocalDate;

public class PouvoirCloseOrderDTO {

    private Long seanceId;

    private LocalDate dateFin;

    private String heureFin;

    public PouvoirCloseOrderDTO() {
    }

    public PouvoirCloseOrderDTO(Long seanceId, LocalDate dateFin, String heureFin) {
        this.seanceId = seanceId;
        this.dateFin = dateFin;
        this.heureFin = heureFin;
    }

    public Long getSeanceId() {
        return seanceId;
    }

    public void setSeanceId(Long seanceId) {
        this.seanceId = seanceId;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public String getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(String heureFin) {
        this.heureFin = heureFin;
    }
}
