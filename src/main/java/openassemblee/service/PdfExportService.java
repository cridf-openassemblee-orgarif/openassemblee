package openassemblee.service;

import static openassemblee.service.EluService.getOnlyCurrentMandat;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.inject.Inject;
import openassemblee.domain.*;
import openassemblee.service.dto.EluEnFonctionDTO;
import openassemblee.service.dto.EluListDTO;
import openassemblee.service.util.EluNomComparator;
import org.springframework.stereotype.Service;

@Service
public class PdfExportService {

    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(
        "dd/MM/yyyy"
    );

    @Inject
    private SessionMandatureService sessionMandatureService;

    public class Rotate extends PdfPageEventHelper {

        protected PdfNumber orientation = PdfPage.PORTRAIT;

        public void setOrientation(PdfNumber orientation) {
            this.orientation = orientation;
        }

        @Override
        public void onStartPage(PdfWriter writer, Document document) {
            writer.addPageDictEntry(PdfName.ROTATE, orientation);
        }
    }

    public byte[] exportElus(List<EluListDTO> elus) throws DocumentException {
        Document document = new Document();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, output);

        document.open();

        document.add(mainTitle("Export des élus"));

        elus
            .stream()
            .sorted(EluNomComparator.comparing(EluListDTO::getElu))
            .forEach(e -> {
                try {
                    addElu(document, e);
                } catch (DocumentException ex) {
                    throw new RuntimeException(ex);
                }
            });

        document.close();
        return output.toByteArray();
    }

    public byte[] exportExecutif(
        List<EluEnFonctionDTO> executif,
        List<EluEnFonctionDTO> fonctions
    ) throws DocumentException {
        Document document = new Document();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, output);

        document.open();

        document.add(mainTitle("Export de l'exécutif"));

        document.add(subTitle2("L'exécutif"));
        addCpElus(document, executif);

        document.add(subTitle2("Les élus avec une fonction"));
        addCpElus(document, fonctions);

        document.close();
        return output.toByteArray();
    }

    public byte[] exportCommissionPermanente(List<EluEnFonctionDTO> elus)
        throws DocumentException {
        Document document = new Document();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, output);

        document.open();

        document.add(mainTitle("Export des élus de la commission permanente"));

        document.add(subTitle2("Les membres de la commission"));
        addCpElus(document, elus);

        document.close();
        return output.toByteArray();
    }

    public byte[] exportCommissionsThematiques(
        List<CommissionThematique> commissionThematiques,
        Map<Long, List<EluEnFonctionDTO>> appartenancesByCt,
        Map<Long, List<EluEnFonctionDTO>> fonctionsByCt
    ) throws DocumentException {
        Document document = new Document();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, output);

        document.open();

        document.add(mainTitle("Export des commissions thématiques"));

        commissionThematiques.forEach(ct -> {
            try {
                document.add(
                    subTitle1("Commission thématique \"" + ct.getNom() + "\"")
                );

                document.add(subTitle2("Les membres de la commission"));
                addCtElus(document, appartenancesByCt.get(ct.getId()));
                document.add(subTitle2("Les élus ayant une fonction"));
                addCtElus(document, fonctionsByCt.get(ct.getId()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        document.close();
        return output.toByteArray();
    }

    public byte[] exportCommissionsThematique(
        CommissionThematique commissionThematique,
        List<EluEnFonctionDTO> appartenances,
        List<EluEnFonctionDTO> fonctions
    ) throws DocumentException {
        Document document = new Document();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, output);

        document.open();

        document.add(
            mainTitle(
                "Export de la commission thématique \"" +
                commissionThematique.getNom() +
                "\""
            )
        );

        document.add(subTitle2("Les élus ayant une fonction"));
        addCtElus(
            document,
            fonctions
                .stream()
                .sorted(EluNomComparator.comparing(EluEnFonctionDTO::getElu))
                .collect(Collectors.toList())
        );
        document.add(subTitle2("Les membres de la commission"));
        addCtElus(
            document,
            appartenances
                .stream()
                .sorted(EluNomComparator.comparing(EluEnFonctionDTO::getElu))
                .collect(Collectors.toList())
        );

        document.close();
        return output.toByteArray();
    }

    public byte[] feuilleEmargement(
        LocalDate date,
        List<Elu> elus,
        int signatureNumber
    ) throws DocumentException {
        Document document = new Document(PageSize.A4.rotate());
        //        document.setPageSize(PageSize.LETTER.rotate());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, output);

        document.open();

        document.add(
            mainTitle(
                "Feuille d'émargement - " +
                (date != null ? dateFormatter.format(date) : "")
            )
        );

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

        table.addCell(header("Nom prénom"));
        //        addHeader(table, "Statut");
        for (int i = 1; i <= signatureNumber; i++) {
            table.addCell(header("Signature " + i));
        }
        table.addCell(header("Pouvoir"));

        elus
            .stream()
            .sorted(EluNomComparator.comparing(e -> e))
            .forEach(e -> addRow(table, e, signatureNumber));

        document.add(table);

        document.close();
        return output.toByteArray();
    }

    private Paragraph mainTitle(String title) {
        Font font = FontFactory.getFont(
            FontFactory.HELVETICA,
            24,
            BaseColor.BLACK
        );
        Chunk chunk = new Chunk(title);
        chunk.setFont(font);
        Paragraph p = new Paragraph(chunk);
        p.setAlignment(Element.ALIGN_CENTER);
        p.setSpacingAfter(40f);
        return p;
    }

    private Paragraph subTitle1(String title) {
        Font font = FontFactory.getFont(
            FontFactory.HELVETICA,
            22,
            BaseColor.BLACK
        );
        Chunk chunk = new Chunk(title);
        chunk.setFont(font);
        Paragraph p = new Paragraph(chunk);
        p.setAlignment(Element.ALIGN_LEFT);
        p.setSpacingAfter(30f);
        return p;
    }

    private Paragraph subTitle2(String title) {
        Font font = FontFactory.getFont(
            FontFactory.HELVETICA,
            20,
            BaseColor.BLACK
        );
        Chunk chunk = new Chunk(title);
        chunk.setFont(font);
        Paragraph p = new Paragraph(chunk);
        p.setAlignment(Element.ALIGN_LEFT);
        p.setSpacingAfter(30f);
        return p;
    }

    private void addElu(Document document, EluListDTO dto)
        throws DocumentException {
        Elu elu = dto.getElu();
        Paragraph title = bigP(
            elu.getCiviliteLabel() + " " + elu.getPrenom() + " " + elu.getNom()
        );
        title.setSpacingBefore(10f);
        document.add(title);
        document.add(
            p(
                "Date de naissance : " +
                (
                    elu.getDateNaissance() != null
                        ? dateFormatter.format(elu.getDateNaissance())
                        : "Inconnue"
                )
            )
        );
        document.add(p("Lieu de naissance : " + elu.getLieuNaissance()));
        //        document.add(p("Groupe politique : " + elu.getAppartenancesGroupePolitique().get(0).getGroupePolitique().getNom()));
        String gpLabel = dto.getGroupePolitique() != null
            ? dto.getGroupePolitique().getNom() +
            "(" +
            dto.getGroupePolitique().getNomCourt() +
            ")"
            : "Non inscrit";
        document.add(p("Groupe politique : " + gpLabel));
        Mandat mandat = getOnlyCurrentMandat(
            elu.getMandats(),
            sessionMandatureService.getMandature()
        );
        String departement = mandat != null ? mandat.getDepartement() : "";
        document.add(p("Circonscription : " + departement));

        document.add(p("Adresses :"));
        document.add(adressePostales(elu.getAdressesPostales()));
        document.add(p("Adresses mail :"));
        document.add(mails(elu.getAdressesMail()));
        document.add(p("Numéros de téléphone :"));
        PdfPTable numeros = numeros(elu.getNumerosTelephones());
        numeros.setSpacingAfter(10f);
        document.add(numeros);

        PdfDiv div = new PdfDiv();
        div.setBackgroundColor(BaseColor.WHITE);
        div.setHeight(30f);
        document.add(div);
        PdfDiv div2 = new PdfDiv();
        div2.setBackgroundColor(BaseColor.BLACK);
        div2.setHeight(1f);
        document.add(div2);
        PdfDiv div3 = new PdfDiv();
        div3.setBackgroundColor(BaseColor.WHITE);
        div3.setHeight(30f);
        document.add(div3);
    }

    private void addCtElus(Document document, List<EluEnFonctionDTO> elus) {
        if (elus != null) {
            elus
                .stream()
                .forEach(e -> {
                    try {
                        Elu elu = e.getElu();
                        String fonctionLabel = e.getFonctionLabel() != null
                            ? e.getFonctionLabel() + " : "
                            : "";
                        Paragraph title = bigP(
                            fonctionLabel +
                            elu.getCiviliteLabel() +
                            " " +
                            elu.getPrenom() +
                            " " +
                            elu.getNom()
                        );
                        title.setSpacingBefore(10f);
                        document.add(title);
                    } catch (DocumentException ex) {
                        throw new RuntimeException(ex);
                    }
                });
        }
    }

    private void addCpElus(Document document, List<EluEnFonctionDTO> elus) {
        if (elus != null) {
            elus
                .stream()
                .forEach(e -> {
                    try {
                        addCpElu(document, e);
                    } catch (DocumentException ex) {
                        throw new RuntimeException(ex);
                    }
                });
        }
    }

    private void addCpElu(Document document, EluEnFonctionDTO dto)
        throws DocumentException {
        Elu elu = dto.getElu();
        Paragraph title = bigP(
            elu.getCiviliteLabel() + " " + elu.getPrenom() + " " + elu.getNom()
        );
        title.setSpacingBefore(10f);
        document.add(title);
        if (dto.getFonctionLabel() != null) {
            document.add(p("Fonction : " + dto.getFonctionLabel()));
        }
        document.add(
            p(
                "Date de naissance : " +
                (
                    elu.getDateNaissance() != null
                        ? dateFormatter.format(elu.getDateNaissance())
                        : "Inconnue"
                )
            )
        );
        document.add(p("Lieu de naissance : " + elu.getLieuNaissance()));
        //        document.add(p("Groupe politique : " + elu.getAppartenancesGroupePolitique().get(0).getGroupePolitique().getNom()));
        String gpLabel = dto.getGroupePolitique() != null
            ? dto.getGroupePolitique().getNom() +
            "(" +
            dto.getGroupePolitique().getNomCourt() +
            ")"
            : "Non inscrit";
        document.add(p("Groupe politique : " + gpLabel));
        Mandat mandat = getOnlyCurrentMandat(
            elu.getMandats(),
            sessionMandatureService.getMandature()
        );
        String departement = mandat != null ? mandat.getDepartement() : "";
        document.add(p("Circonscription : " + departement));

        document.add(p("Adresses :"));
        document.add(adressePostales(elu.getAdressesPostales()));
        document.add(p("Adresses mail :"));
        document.add(mails(elu.getAdressesMail()));
        document.add(p("Numéros de téléphone :"));
        PdfPTable numeros = numeros(elu.getNumerosTelephones());
        numeros.setSpacingAfter(10f);
        document.add(numeros);

        PdfDiv div = new PdfDiv();
        div.setBackgroundColor(BaseColor.WHITE);
        div.setHeight(30f);
        document.add(div);
        PdfDiv div2 = new PdfDiv();
        div2.setBackgroundColor(BaseColor.BLACK);
        div2.setHeight(1f);
        document.add(div2);
        PdfDiv div3 = new PdfDiv();
        div3.setBackgroundColor(BaseColor.WHITE);
        div3.setHeight(30f);
        document.add(div3);
    }

    private Paragraph bigP(String text) {
        Font font = FontFactory.getFont(
            FontFactory.HELVETICA,
            14,
            BaseColor.BLACK
        );
        Chunk chunk = new Chunk(text);
        chunk.setFont(font);
        Paragraph p = new Paragraph(chunk);
        p.setAlignment(Element.ALIGN_LEFT);
        p.setSpacingAfter(6f);
        return p;
    }

    private Paragraph p(String text) {
        //        Font font = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
        //        Chunk chunk = new Chunk(elu.getCiviliteLabel() + " " + elu.getPrenom() + " " + elu.getNom());
        //        chunk.setFont(font);
        Paragraph p = new Paragraph(text);
        p.setAlignment(Element.ALIGN_LEFT);
        p.setSpacingAfter(4f);
        return p;
    }

    private PdfPTable adressePostales(List<AdressePostale> aps)
        throws DocumentException {
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setWidths(new float[] { 1, 1, 1 });

        table.addCell(header("Nature"));
        table.addCell(header("Niveau de confidentialité"));
        table.addCell(header("Adresse"));

        aps
            .stream()
            .filter(a ->
                a.getPublicationAnnuaire() != null && a.getPublicationAnnuaire()
            )
            .forEach(a -> {
                table.addCell(
                    a.getNatureProPerso() != null
                        ? a.getNatureProPerso().name()
                        : ""
                );
                table.addCell(
                    a.getNiveauConfidentialite() != null
                        ? a.getNiveauConfidentialite().name()
                        : ""
                );
                table.addCell(a.getOneline());
            });

        return table;
    }

    private PdfPTable mails(List<AdresseMail> mails) throws DocumentException {
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setWidths(new float[] { 1, 1, 1 });

        table.addCell(header("Nature"));
        table.addCell(header("Niveau de confidentialité"));
        table.addCell(header("Email"));

        mails
            .stream()
            .filter(m -> m.getPublicationAnnuaire())
            .forEach(m -> {
                table.addCell(
                    m.getNatureProPerso() != null
                        ? m.getNatureProPerso().name()
                        : ""
                );
                table.addCell(
                    m.getNiveauConfidentialite() != null
                        ? m.getNiveauConfidentialite().name()
                        : ""
                );
                table.addCell(m.getMail());
            });

        return table;
    }

    private PdfPTable numeros(List<NumeroTelephone> numeros)
        throws DocumentException {
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setWidths(new float[] { 1, 1, 1 });

        table.addCell(header("Nature"));
        table.addCell(header("Niveau de confidentialité"));
        table.addCell(header("Numéro"));

        numeros
            .stream()
            .filter(n -> n.getPublicationAnnuaire())
            .forEach(n -> {
                table.addCell(
                    n.getNatureProPerso() != null
                        ? n.getNatureProPerso().name()
                        : ""
                );
                table.addCell(
                    n.getNiveauConfidentialite() != null
                        ? n.getNiveauConfidentialite().name()
                        : ""
                );
                table.addCell(n.getNumero());
            });

        return table;
    }

    private PdfPCell header(String headerTitle) {
        PdfPCell cell = new PdfPCell(new Phrase(headerTitle));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return cell;
    }

    private void addRow(PdfPTable table, Elu elu, int signatureNumber) {
        table.addCell(cell(elu.getNom() + " " + elu.getPrenom()));
        //        addCell(table, "Titulaire");
        for (int i = 1; i <= signatureNumber; i++) {
            table.addCell(cell(""));
        }
        table.addCell(cell(""));
    }

    private PdfPCell cell(String text) {
        Font font = FontFactory.getFont(
            FontFactory.HELVETICA,
            8,
            BaseColor.BLACK
        );
        Phrase phrase = new Phrase(text);
        phrase.setFont(font);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(50f);
        cell.setPadding(5f);
        return cell;
    }
}
