package openassemblee.service.dto;

import openassemblee.domain.Elu;

import java.util.List;

public class CandidatCorrespondanceDTO {

    private CandidatDTO candidatDTO;
    private Elu elu;
    private Boolean tropDeCorrespondance;
    private List<String> errors;

    public CandidatCorrespondanceDTO() {
    }

    public CandidatCorrespondanceDTO(CandidatDTO candidatDTO, Elu elu, Boolean tropDeCorrespondance, List<String> errors) {
        this.candidatDTO = candidatDTO;
        this.elu = elu;
        this.tropDeCorrespondance = tropDeCorrespondance;
        this.errors = errors;
    }

    public CandidatDTO getCandidatDTO() {
        return candidatDTO;
    }

    public Elu getElu() {
        return elu;
    }

    public Boolean getTropDeCorrespondance() {
        return tropDeCorrespondance;
    }

    public List<String> getErrors() {
        return errors;
    }
}
