package openassemblee.service;

import com.itextpdf.text.DocumentException;
import openassemblee.domain.Elu;
import org.apache.commons.io.IOUtils;
import org.junit.Ignore;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PdfServiceTest {

    @Ignore
    @Test
    public void testExportPdf() throws IOException, DocumentException {
        PdfService service = new PdfService();
        List<Elu> elus = new ArrayList();
        Elu testElu = new Elu();
        testElu.setNom("Chirac");
        testElu.setPrenom("Jacques");
        elus.add(testElu);
        byte[] pdf = service.export(elus, 2);
        IOUtils.copy(new ByteArrayInputStream(pdf),
            new FileOutputStream("/Users/mlo/git/openassemblee/test.pdf"));
    }

}
