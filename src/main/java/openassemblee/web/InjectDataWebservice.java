package openassemblee.web;

import openassemblee.domain.AdressePostale;
import openassemblee.domain.Elu;
import openassemblee.domain.NumeroFax;
import openassemblee.domain.NumeroTelephone;
import openassemblee.domain.enumeration.Civilite;
import openassemblee.publicdata.ConseillerDto;
import openassemblee.publicdata.DataBag;
import openassemblee.publicdata.EnsembleDto;
import openassemblee.publicdata.MembreDto;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static openassemblee.domain.enumeration.NiveauConfidentialite.PUBLIABLE;

@RestController
@RequestMapping("/api/inject/v1")
public class InjectDataWebservice {

    Map<String,Elu> elus ;

    @RequestMapping(value = "/data", method = RequestMethod.GET)
    public String ping() {
        return "ping";
    }

    @RequestMapping(value = "/data", method = RequestMethod.POST)
    @Transactional
    public void data(@RequestBody DataBag data) {
        elus = new HashMap<>();
        List<ConseillerDto> conseillers = data.getConseillers();
        List<EnsembleDto> ensembles = data.getEnsembles();
        List<MembreDto> membres = data.getMembres();
        System.out.println(conseillers.size());
        conseillers.forEach( c -> {
            Elu elu = BuildElu(c);
            elus.put(c.getUidConseiller(),elu);
        });
    }

    private Elu BuildElu(ConseillerDto c) {
        Elu elu = new Elu();
        elu.setCivilite(Civilite.valueOf(c.getCivilite()));
        elu.setNom(c.getNom());
        elu.setPrenom(c.getPrenom());
        elu.setNomJeuneFille(c.getNomJeuneFille());
        elu.setProfession(c.getProfession());
        elu.setDateNaissance(LocalDate.parse(c.getDateNaissance()));
        elu.setLieuNaissance(c.getVilleNaissance());
        //TODO
        elu.setDepartement("");

        //TODO Check
        ArrayList<AdressePostale> adressesPostales = new ArrayList<>();
        adressesPostales.add(new AdressePostale());
        AdressePostale adresse = adressesPostales.get(0);
        adresse.setNiveauConfidentialite(PUBLIABLE);
        adresse.setPublicationAnnuaire(true);
        adresse.setVille(c.getVille());
        adresse.setCodePostal(c.getCodePostal());
        adresse.setVoie(c.getAdresse());
        elu.setAdressesPostales(adressesPostales);

        //TODO Check Nature
        ArrayList<NumeroTelephone> numeroTelephones = new ArrayList<>();
        numeroTelephones.add(new NumeroTelephone());
        NumeroTelephone numeroTelephone = numeroTelephones.get(0);
        numeroTelephone.setNiveauConfidentialite(PUBLIABLE);
        numeroTelephone.setPublicationAnnuaire(true);
        numeroTelephone.setNumero(c.getTelephone());
        elu.setNumerosTelephones(numeroTelephones);

        ArrayList<NumeroFax> numeroFaxs = new ArrayList<>();
        numeroFaxs.add(new NumeroFax());
        NumeroFax numeroFax = numeroFaxs.get(0);
        numeroFax.setNiveauConfidentialite(PUBLIABLE);
        numeroFax.setPublicationAnnuaire(true);
        numeroFax.setNumero(c.getTelephone());
        elu.setNumerosFax(numeroFaxs);

//        elu.setAdressesMail(c.get());
//        elu.setIdentitesInternet(c.get());
//        elu.setFonctionsExecutives(c.get());
//        elu.setFonctionsCommissionPermanente(c.get());
//        elu.setAppartenancesCommissionPermanente(c.get());
//        elu.setAppartenancesGroupePolitique(c.get());
//        elu.setFonctionsGroupePolitique(c.get());
//        elu.setAppartenancesCommissionsThematiques(c.get());
//        elu.setFonctionsCommissionsThematiques(c.get());
//        elu.setImage(c.get());
//        elu.setMotifDemission(c.get());
//        elu.setDateDemission(c.get());
//        elu.setAppartenancesOrganismes(c.get());
//        elu.setAutreMandats(c.get());

        //TODO c'est bien ça ?
        elu.setUuid( c.getUidConseiller());
        return elu;
    }

}
