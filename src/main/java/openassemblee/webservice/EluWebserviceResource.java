package openassemblee.webservice;

import openassemblee.domain.enumeration.Civilite;
import openassemblee.service.EluService;
import openassemblee.service.SessionMandatureService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import org.slf4j.Logger;
import java.util.stream.Collectors;

import static openassemblee.service.EluService.isCurrentMandat;

@RestController
@RequestMapping("/api/publicdata/v2")
public class EluWebserviceResource {

    @Autowired
    protected EluService eluService;

    @Autowired
    private SessionMandatureService sessionMandatureService;

    enum PublicCivilite {M, MME}

    class PublicElu {
        public String id;
        public String uid;
        public PublicCivilite civilite;
        public String nom;
        public String prenom;
        public String groupePolitique;
        public String groupePolitiqueCourt;
        public String image;
        public Boolean actif;

        public PublicElu(String id, String uid, PublicCivilite civilite, String nom, String prenom,
                         String groupePolitique, String groupePolitiqueShort, String image, Boolean actif) {
            this.id = id;
            this.uid = uid;
            this.civilite = civilite;
            this.nom = nom;
            this.prenom = prenom;
            this.groupePolitique = groupePolitique;
            this.groupePolitiqueCourt = groupePolitiqueShort;
            this.image = image;
            this.actif = actif;
        }
    }

    class Data {
        public List<PublicElu> elus;

        public Data(List<PublicElu> elus) {
            this.elus = elus;
        }
    }

    @RequestMapping(value = "/elus", method = RequestMethod.GET)
    public Data elus() {
        List<PublicElu> elus = eluService.getAll(false, false, false)
            .stream()
            .map(it -> {
                String groupePolitique = it.getGroupePolitique() != null ? it.getGroupePolitique().getNom() :
                    "sans groupe";
                String groupePolitiqueCourt = it.getGroupePolitique() != null ? it.getGroupePolitique().getNomCourt() :
                    "-";
                return new PublicElu(
                    it.getElu().getShortUid().toString(),
                    it.getElu().getUid().replace("-", ""),
                    it.getElu().getCivilite() == Civilite.MONSIEUR ? PublicCivilite.M : PublicCivilite.MME,
                    it.getElu().getNom(),
                    it.getElu().getPrenom(),
                    groupePolitique,
                    groupePolitiqueCourt,
                    it.getElu().getImage() != null ? "/images/" + it.getElu().getImage() : null,
                    isCurrentMandat(it.getElu().getMandats(), sessionMandatureService.getMandature()));
            })
            .collect(Collectors.toList());
        return new Data(elus);
    }

}
