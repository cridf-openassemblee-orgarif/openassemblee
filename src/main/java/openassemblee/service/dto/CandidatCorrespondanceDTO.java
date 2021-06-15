package openassemblee.service.dto;

import openassemblee.domain.Elu;

import java.util.List;

public class CandidatCorrespondanceDTO {

    private CandidatDTO candidatDTO;
    private Elu elu;
    private Boolean tropDeCorrespondances;
    private List<String> errors;

    public CandidatCorrespondanceDTO() {
    }

    public CandidatCorrespondanceDTO(CandidatDTO candidatDTO, Elu elu, Boolean tropDeCorrespondances, List<String> errors) {
        this.candidatDTO = candidatDTO;
        this.elu = elu;
        this.tropDeCorrespondances = tropDeCorrespondances;
        this.errors = errors;
    }

    public CandidatDTO getCandidatDTO() {
        return candidatDTO;
    }

    public Elu getElu() {
        return elu;
    }

    public Boolean getTropDeCorrespondances() {
        return tropDeCorrespondances;
    }

    public List<String> getErrors() {
        return errors;
    }
}
