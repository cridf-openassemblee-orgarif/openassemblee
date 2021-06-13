package openassemblee.service;

import openassemblee.domain.Mandature;
import openassemblee.repository.MandatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SessionMandatureService {

    private Mandature forceMandature;

    @Autowired
    private MandatureRepository mandatureRepository;

    public Mandature getMandature() {
        if (forceMandature != null) {
            // current mandature pourrait avoir changé mais bon... plus malin de ne rien changer ici
            // si deux personnes travaillent en parallèle, une peut penser avoir mandature fixée
            // et en fait on peut lui la virer puis revenir à la mandature précédente...
            // le mieux c'est de changer depuis le front pour celui qui fait les changements
            // de mandature courante uniquement
            //Mandature mandature = mandatureRepository.findOneByCurrent(true);
            //if (mandature.getId().equals(forceMandature.getId())) {
            //forceMandature = null;
            //return mandature;
            //}
            return forceMandature;
        } else {
            return mandatureRepository.findOneByCurrent(true);
        }
    }

    public Boolean hasForcedMandature() {
        return forceMandature != null;
    }

    public void setMandature(Mandature mandature) {
        Mandature currentMandature = mandatureRepository.findOneByCurrent(true);
        if (mandature.getId().equals(currentMandature.getId())) {
            forceMandature = null;
        } else {
            forceMandature = mandature;
        }
    }
}
