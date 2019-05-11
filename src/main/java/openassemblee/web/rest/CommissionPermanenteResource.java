package openassemblee.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.itextpdf.text.DocumentException;
import openassemblee.service.CommissionPermanenteService;
import openassemblee.service.EluService;
import openassemblee.service.ExcelExportService;
import openassemblee.service.ExcelExportService.Entry;
import openassemblee.service.PdfExportService;
import openassemblee.service.dto.CommissionPermanenteDTO;
import openassemblee.service.dto.EluEnFonctionDTO;
import openassemblee.service.dto.EluListDTO;
import openassemblee.service.dto.ExecutifDTO;
import openassemblee.service.util.SecurityUtil;
import org.elasticsearch.common.io.Streams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
// TODO test
public class CommissionPermanenteResource {

    @Autowired
    private CommissionPermanenteService commissionPermanenteService;

    @Autowired
    private ExcelExportService excelExportService;
    @Autowired
    private PdfExportService pdfExportService;
    @Autowired
    private EluService eluService;

    @RequestMapping(value = "/executif",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ExecutifDTO> getExecutif() {
        return new ResponseEntity<>(commissionPermanenteService.getExecutif(false), HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    @RequestMapping(value = "/executif/export",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured("ROLE_USER")
    public void exportExecutif(HttpServletResponse response) {
        Entry[] entries = commissionPermanenteService.getExecutifExportEntries();
        byte[] export = excelExportService.exportToExcel(entries);

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

    @Transactional(readOnly = true)
    @RequestMapping(value = "/executif/export-pdf",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured("ROLE_USER")
    public void exportExecutPdf(HttpServletResponse response, Authentication auth) throws DocumentException {
        ExecutifDTO dto = commissionPermanenteService.getExecutif(true);
        Boolean filterAdresses = !SecurityUtil.isAdmin(auth);
        List<EluEnFonctionDTO> executif = dto.getFonctionsExecutives().stream().map(fe -> {
            EluListDTO listDto = eluService.eluToEluListDTO(fe.getElu(), true, filterAdresses);
            return new EluEnFonctionDTO(fe.getElu(), listDto.getGroupePolitique(), fe.getFonction());
        }).collect(Collectors.toList());
        List<EluEnFonctionDTO> fonctions = dto.getFonctions().stream().map(fe -> {
            EluListDTO listDto = eluService.eluToEluListDTO(fe.getElu(), true, filterAdresses);
            return new EluEnFonctionDTO(fe.getElu(), listDto.getGroupePolitique(), fe.getFonction());
        }).collect(Collectors.toList());
        byte[] export = pdfExportService.exportExecutif(executif, fonctions);

        response.setContentType("application/pdf");
        String filename = "siger-commission-permanente";
        response.setHeader("Content-disposition", "attachment; filename=" + filename + ".pdf");
        try {
            Streams.copy(export, response.getOutputStream());
        } catch (IOException e1) {
            // TODO exception
            e1.printStackTrace();
        }
    }

    @RequestMapping(value = "/commission-permanente",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CommissionPermanenteDTO> getCommissionPermanente() {
        return new ResponseEntity<>(commissionPermanenteService.getCommissionPermanente(false), HttpStatus.OK);
    }

    @RequestMapping(value = "/commission-permanente/export",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured("ROLE_USER")
    public void export(HttpServletResponse response) {
        Entry[] entries = commissionPermanenteService.getCommissionPermanenteExportEntries();
        byte[] export = excelExportService.exportToExcel(entries);

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

    @Transactional(readOnly = true)
    @RequestMapping(value = "/commission-permanente/export-pdf",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured("ROLE_USER")
    public void exportPdf(HttpServletResponse response, Authentication auth) throws DocumentException {
        List<EluEnFonctionDTO> as = commissionPermanenteService
            .getAppartenancesCommissionPermanenteDtos(!SecurityUtil.isAdmin(auth), true);

        byte[] export = pdfExportService.exportCommissionPermanente(as);

        response.setContentType("application/pdf");
        String filename = "siger-commission-permanente";
        response.setHeader("Content-disposition", "attachment; filename=" + filename + ".pdf");
        try {
            Streams.copy(export, response.getOutputStream());
        } catch (IOException e1) {
            // TODO exception
            e1.printStackTrace();
        }
    }

}
