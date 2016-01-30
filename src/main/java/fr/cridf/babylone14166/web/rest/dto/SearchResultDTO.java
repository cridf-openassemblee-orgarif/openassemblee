package fr.cridf.babylone14166.web.rest.dto;

public class SearchResultDTO {

    public enum ResultType {
        ELU, GROUPE_POLITIQUE, COMMISSION_THEMATIQUE;
    }

    private ResultType resultType;
    private long id;
    private String display;
    private Long image;

    public SearchResultDTO(ResultType resultType, long id, String display, Long image) {
        this.resultType = resultType;
        this.id = id;
        this.display = display;
        this.image = image;
    }

    public ResultType getResultType() {
        return resultType;
    }

    public long getId() {
        return id;
    }

    public String getDisplay() {
        return display;
    }

    public Long getImage() {
        return image;
    }
}
