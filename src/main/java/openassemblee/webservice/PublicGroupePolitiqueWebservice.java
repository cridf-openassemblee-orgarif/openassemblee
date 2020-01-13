package openassemblee.webservice;

import openassemblee.domain.GroupePolitique;
import openassemblee.domain.enumeration.Civilite;
import openassemblee.service.EluService;
import openassemblee.service.GroupePolitiqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/publicdata/v2")
public class PublicGroupePolitiqueWebservice {

    @Autowired
    protected GroupePolitiqueService groupePolitiqueService;

    class PublicGroupePolitique {
        public String id;
        public String uid;
        public String nom;
        public String nomCourt;
        public String couleur;
        public String image;
        public Boolean actif;

        public PublicGroupePolitique(String id, String uid, String nom, String nomCourt, String couleur, String image, Boolean actif) {
            this.id = id;
            this.uid = uid;
            this.nom = nom;
            this.nomCourt = nomCourt;
            this.couleur = couleur;
            this.image = image;
            this.actif = actif;
        }
    }

    class Data {
        public List<PublicGroupePolitique> groupesPolitiques;

        public Data(List<PublicGroupePolitique> groupesPolitiques) {
            this.groupesPolitiques = groupesPolitiques;
        }
    }

    @RequestMapping(value = "/groupe-politique", method = RequestMethod.GET)
    public Data elus() {
        List<PublicGroupePolitique> elus = groupePolitiqueService.getAll()
            .stream()
            .map(it -> {
                return new PublicGroupePolitique(
                    it.getGroupePolitique().getShortUid().toString(),
                    it.getGroupePolitique().getUid(),
                    it.getGroupePolitique().getNom(),
                    it.getGroupePolitique().getNomCourt(),
                    it.getGroupePolitique().getCouleur(),
                    "/images/" + it.getGroupePolitique().getImage(),
                    it.getGroupePolitique().getDateFin() != null);
            })
            .collect(Collectors.toList());
        return new Data(elus);
    }

}
