package openassemblee.web.rest;

import com.codahale.metrics.annotation.Timed;
import openassemblee.service.CommissionPermanenteService;
import openassemblee.service.ExportService;
import openassemblee.service.ExportService.Entry;
import openassemblee.service.dto.CommissionPermanenteDTO;
import org.elasticsearch.common.io.Streams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api")
// TODO test
public class CommissionPermanenteResource {

    @Autowired
    private CommissionPermanenteService commissionPermanenteService;

    @Autowired
    private ExportService exportService;

    @RequestMapping(value = "/commission-permanente",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CommissionPermanenteDTO> getCommissionPermanente() {
        return new ResponseEntity<>(commissionPermanenteService.getCommissionPermanente(), HttpStatus.OK);
    }

    @RequestMapping(value = "/commission-permanente/export",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void export(HttpServletResponse response) {
        Entry[] entries = commissionPermanenteService.getExportEntries();
        byte[] export = exportService.exportToExcel(entries);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String filename = "siger-export-elus";
        response.setHeader("Content-disposition", "attachment; filename=" + filename + ".xlsx");
        try {
            Streams.copy(export, response.getOutputStream());
        } catch (IOException e) {
            // TODO exception
            e.printStackTrace();
        }
    }

}
