package openassemblee.publicdata;

import java.util.List;

public class DataBag {

    private List<ConseillerDto> conseillers;
    private List<EnsembleDto> ensembles;
    private List<MembreDto> membres;

    public List<ConseillerDto> getConseillers() {
        return conseillers;
    }

    public void setConseillers(List<ConseillerDto> conseillers) {
        this.conseillers = conseillers;
    }

    public List<EnsembleDto> getEnsembles() {
        return ensembles;
    }

    public void setEnsembles(List<EnsembleDto> ensembles) {
        this.ensembles = ensembles;
    }

    public List<MembreDto> getMembres() {
        return membres;
    }

    public void setMembres(List<MembreDto> membres) {
        this.membres = membres;
    }
}
