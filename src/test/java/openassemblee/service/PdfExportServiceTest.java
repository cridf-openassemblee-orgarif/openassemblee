package openassemblee.service;

import com.itextpdf.text.DocumentException;
import openassemblee.domain.*;
import openassemblee.domain.enumeration.Civilite;
import openassemblee.domain.enumeration.NatureFixeMobile;
import openassemblee.domain.enumeration.NatureProPerso;
import openassemblee.domain.enumeration.NiveauConfidentialite;
import openassemblee.service.dto.EluListDTO;
import org.apache.commons.io.IOUtils;
import org.junit.Ignore;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PdfExportServiceTest {

    private PdfExportService pdfExportService = new PdfExportService();

    @Ignore
    @Test
    public void testFeuilleEmargement() throws IOException, DocumentException {
        LocalDate today = LocalDate.now(ZoneId.of("Europe/Paris"));
        byte[] pdf = pdfExportService.feuilleEmargement(today, elus(), 2);
        IOUtils.copy(new ByteArrayInputStream(pdf),
            new FileOutputStream("/Users/mlo/git/openassemblee/test.pdf"));
    }

    @Ignore
    @Test
    public void testExportPdf() throws IOException, DocumentException {
        PdfExportService service = new PdfExportService();
        byte[] pdf = service.exportElus(eluDtos());
        IOUtils.copy(new ByteArrayInputStream(pdf),
            new FileOutputStream("/Users/mlo/Downloads/test-export-elus.pdf"));
    }

    private List<Elu> elus() {
        List<Elu> elus = new ArrayList<>();
        Elu e = new Elu();
        e.setCivilite(Civilite.MONSIEUR);
        e.setNom("ADLANI");
        e.setPrenom("Farida");
        e.setDateNaissance(LocalDate.of(1976, 3, 11));
        e.setLieuNaissance("Inconnu");
        e.setProfession("Employée (secteur privé)");
//        e.setCodeDepartement("Seine-Saint-Denis");
//        e.setAppartenancesGroupePolitique();
        e.setAdressesPostales(Arrays.asList(
            new AdressePostale(NatureProPerso.PERSO, "5 rue Amelot", "75011", "Paris",
                NiveauConfidentialite.PUBLIABLE, true, true))
        );
        e.setNumerosTelephones(Arrays.asList(
            new NumeroTelephone(NatureProPerso.PERSO, NatureFixeMobile.MOBILE, "0612345678",
                NiveauConfidentialite.PUBLIABLE, true)
        ));
        e.setAdressesMail(Arrays.asList(
            new AdresseMail(NatureProPerso.PERSO, "test@test.com", NiveauConfidentialite.PUBLIABLE, true, true)
        ));
        elus.add(e);
        Elu e1 = new Elu();
        e1.setCivilite(Civilite.MONSIEUR);
        e1.setNom("AESCHLIMANN");
        e1.setPrenom("Marie-Do");
        e1.setDateNaissance(LocalDate.of(1974, 4, 17));
        e1.setLieuNaissance("Basse-Terre");
        e1.setProfession("Avocate");
//        e1.setCodeDepartement("Hauts-de-Seine");
//        e. setAppartenancesGroupePolitique();
        e1.setAdressesPostales(Arrays.asList(
            new AdressePostale(NatureProPerso.PERSO, "10 rue Boulle", "75011", "Paris",
                NiveauConfidentialite.PUBLIABLE, true, true))
        );
        elus.add(e1);
        return elus;
    }

    private List<EluListDTO> eluDtos() {
        List<Elu> elus = elus();
        GroupePolitique gp = new GroupePolitique();
        gp.setNom("Groupe Alternative écologiste et sociale");
        gp.setNomCourt("AES");
        return elus.stream().map(elu -> new EluListDTO(elu, gp, "VP", true, true, false)).collect(Collectors.toList());
    }
}
