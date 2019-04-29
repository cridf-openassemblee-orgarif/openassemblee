package openassemblee.service;

import openassemblee.domain.FonctionExecutive;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommissionPermanenteServiceTest {

    private CommissionPermanenteService service = new CommissionPermanenteService();

    @Test
    public void checkScore() {
        Assert.assertEquals(0, service.fonctionScore(fonctionExecutive("Présidente du Conseil régional")));
        Assert.assertEquals(2, service.fonctionScore(fonctionExecutive("2ème Vice-Présidente en charge des lycées et de l'administration générale")));
        Assert.assertEquals(7, service.fonctionScore(fonctionExecutive("7ème Vice-Président en charge de l'attractivité, du logement et de la rénovation urbaine")));
        Assert.assertEquals(9, service.fonctionScore(fonctionExecutive("9ème Vice-Président en charge des sports, des loisirs, de la jeunesse, de la citoyenneté et de la vie associative")));
        Assert.assertEquals(14, service.fonctionScore(fonctionExecutive("14ème Vice-Président en charge de l'écologie et du développement durable")));
    }

    @Test
    public void checkSorting() {
        List<FonctionExecutive> fcts = Arrays.asList(
            fonctionExecutive("9ème Vice-Président en charge des sports, des loisirs, de la jeunesse, de la citoyenneté et de la vie associative"),
            fonctionExecutive("2ème Vice-Présidente en charge des lycées et de l'administration générale"),
            fonctionExecutive("14ème Vice-Président en charge de l'écologie et du développement durable"),
            fonctionExecutive("Présidente du Conseil régional"),
            fonctionExecutive("7ème Vice-Président en charge de l'attractivité, du logement et de la rénovation urbaine")
        );
        List<FonctionExecutive> sorted = fcts.stream().sorted(service::sortFonctionExecutives).collect(Collectors.toList());
        Assert.assertEquals("Présidente du Conseil régional", sorted.get(0).getFonction());
        Assert.assertEquals("2ème Vice-Présidente en charge des lycées et de l'administration générale", sorted.get(1).getFonction());
        Assert.assertEquals("7ème Vice-Président en charge de l'attractivité, du logement et de la rénovation urbaine", sorted.get(2).getFonction());
        Assert.assertEquals("9ème Vice-Président en charge des sports, des loisirs, de la jeunesse, de la citoyenneté et de la vie associative", sorted.get(3).getFonction());
    }

    private FonctionExecutive fonctionExecutive(String fonction) {
        FonctionExecutive fe = new FonctionExecutive();
        fe.setFonction(fonction);
        return fe;
    }

}
