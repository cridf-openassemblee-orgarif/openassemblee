package openassemblee.service.util;

import openassemblee.domain.Elu;
import org.hibernate.Hibernate;

import java.util.stream.Collectors;

import static openassemblee.domain.enumeration.NiveauConfidentialite.PUBLIABLE;

public class ConfidentialiteUtil {
    public static void filterElu(Elu elu, Boolean loadAdresses, Boolean filterAdresses) {
        if (loadAdresses) {
            if (filterAdresses) {
                elu.setAdressesMail(elu.getAdressesMail().stream()
                    .filter(a -> a.getNiveauConfidentialite() == PUBLIABLE)
                    .collect(Collectors.toList()));
                elu.setAdressesPostales(elu.getAdressesPostales().stream()
                    .filter(a -> a.getNiveauConfidentialite() == PUBLIABLE)
                    .collect(Collectors.toList()));
                elu.setNumerosTelephones(elu.getNumerosTelephones().stream()
                    .filter(a -> a.getNiveauConfidentialite() == PUBLIABLE)
                    .collect(Collectors.toList()));
                elu.setNumerosFax(elu.getNumerosFax().stream()
                    .filter(a -> a.getNiveauConfidentialite() == PUBLIABLE)
                    .collect(Collectors.toList()));
            } else {
                Hibernate.initialize(elu.getAdressesMail());
                Hibernate.initialize(elu.getAdressesPostales());
                Hibernate.initialize(elu.getNumerosTelephones());
                Hibernate.initialize(elu.getNumerosFax());
            }
        } else {
            // par mesure de sécurité, pour que ça se voit si on met des paramètre incohérents
            if (filterAdresses) {
                throw new RuntimeException("loadAdresses false && filterAdresses true");
            } else {
                // ne pourront plus être chargés par Hibernate par la suite (réduit marge d'erreur...)
                // le loadAdresses = false tenant de l'optimisation nécessaire au départ
                elu.setAdressesPostales(null);
                elu.setAdressesMail(null);
                elu.setNumerosTelephones(null);
                elu.setNumerosFax(null);
            }

        }
    }
}
