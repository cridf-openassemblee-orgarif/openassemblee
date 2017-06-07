package openassemblee.web;

import openassemblee.publicdata.ConseillerDto;
import openassemblee.publicdata.DataBag;
import openassemblee.publicdata.EnsembleDto;
import openassemblee.publicdata.MembreDto;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/inject/v1")
public class InjectDataWebservice {

    @RequestMapping(value = "/data", method = RequestMethod.GET)
    public String ping() {
        return "ping";
    }

    @RequestMapping(value = "/data", method = RequestMethod.POST)
    @Transactional
    public void data(@RequestBody DataBag data) {
        List<ConseillerDto> conseillers = data.getConseillers();
        List<EnsembleDto> ensembles = data.getEnsembles();
        List<MembreDto> membres = data.getMembres();
        System.out.println(conseillers.size());
    }

}
