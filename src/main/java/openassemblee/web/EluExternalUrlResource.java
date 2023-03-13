package openassemblee.web;

import openassemblee.domain.Elu;
import openassemblee.repository.EluRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.sql.SQLException;
import java.util.UUID;

@Controller
public class EluExternalUrlResource {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private EluRepository eluRepository;

    @RequestMapping(value = "/elu-uid/{id}", method = RequestMethod.GET)
    public String getImage(@PathVariable String id) {
        try {
            String uuid = deserializeUUID(id).toString();
            Elu elu = eluRepository.findOneByUid(uuid);
            return "redirect:/#/elus/" + elu.getId();
        } catch (SQLException e) {
            logger.error("Unable to find elu " + id, e);
            return "redirect:/";
        }
    }

    private UUID deserializeUUID(String stringUuid) {
        return UUID.fromString(stringUuid.substring(0, 8) +
            "-" + stringUuid.substring(8, 12) +
            "-" + stringUuid.substring(12, 16) +
            "-" + stringUuid.substring(16, 20) +
            "-" + stringUuid.substring(20));
    }
}
