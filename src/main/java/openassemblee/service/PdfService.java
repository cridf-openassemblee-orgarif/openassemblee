package openassemblee.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import openassemblee.domain.Elu;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@Service
public class PdfService {

    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public byte[] export(List<Elu> elus, int signatureNumber) throws DocumentException {
        Document document = new Document();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, output);

        document.open();

        // FIXMENOW pas today mais date de la séance coco ?
        LocalDate today = LocalDate.now(ZoneId.of("Europe/Paris"));

        Font font = FontFactory.getFont(FontFactory.HELVETICA, 24, BaseColor.BLACK);
        Chunk chunk = new Chunk("Feuille d'émargement - " + dateFormatter.format(today));
        chunk.setFont(font);
        Paragraph p = new Paragraph(chunk);
        p.setAlignment(Element.ALIGN_CENTER);
        p.setSpacingAfter(40f);
        document.add(p);

        int columnsNumber = 2 + signatureNumber;
        PdfPTable table = new PdfPTable(columnsNumber);
        table.setWidthPercentage(100);
        float[] widths = new float[columnsNumber];
        widths[0] = 3;
        for (int i = 1; i <= signatureNumber; i++) {
            widths[i] = 2;
        }
        widths[columnsNumber - 1] = 2;
        table.setWidths(widths);

        addHeader(table, "Nom prénom");
//        addHeader(table, "Statut");
        for (int i = 1; i <= signatureNumber; i++) {
            addHeader(table, "Signature " + i);
        }
        addHeader(table, "Pouvoir");

        elus
            .stream()
            .sorted(Comparator.comparing(Elu::getNom))
            .forEach(e -> addRow(table, e, signatureNumber));

        document.add(table);

        document.close();
        return output.toByteArray();
    }

    private void addHeader(PdfPTable table, String headerTitle) {
        PdfPCell cell = new PdfPCell(new Phrase(headerTitle));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

    private void addRow(PdfPTable table, Elu elu, int signatureNumber) {
        addCell(table, elu.getNom() + " " + elu.getPrenom());
//        addCell(table, "Titulaire");
        for (int i = 1; i <= signatureNumber; i++) {
            addCell(table, "");
        }
        addCell(table, "");
    }

    private void addCell(PdfPTable table, String text) {
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 8, BaseColor.BLACK);
        Phrase phrase = new Phrase(text);
        phrase.setFont(font);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(30f);
        cell.setPadding(5f);
        table.addCell(cell);
    }

}
