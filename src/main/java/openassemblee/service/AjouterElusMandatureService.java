package openassemblee.service;

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import openassemblee.config.Constants;
import openassemblee.domain.*;
import openassemblee.domain.enumeration.Civilite;
import openassemblee.repository.EluRepository;
import openassemblee.repository.ListeElectoraleRepository;
import openassemblee.repository.MandatRepository;
import openassemblee.repository.MandatureRepository;
import openassemblee.service.dto.CandidatCorrespondanceDTO;
import openassemblee.service.dto.CandidatDTO;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AjouterElusMandatureService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private EluRepository eluRepository;

    @Autowired
    private MandatureRepository mandatureRepository;

    @Autowired
    private MandatRepository mandatRepository;

    @Autowired
    private ListeElectoraleRepository listeElectoraleRepository;

    @Autowired
    private ShortUidService shortUidService;

    private CSVFormat format = CSVFormat.DEFAULT.withDelimiter(';');

    public List<CandidatCorrespondanceDTO> prepareImport(String csvData) {
        List<CSVRecord> records;
        try {
            records = format.parse(new StringReader(csvData)).getRecords();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<CandidatDTO> candidats = records
            .stream()
            .map(this::mapCandidat)
            .collect(Collectors.toList());
        List<Elu> elus = eluRepository.findAll();
        List<CandidatCorrespondanceDTO> result = candidats
            .stream()
            .map(c -> {
                List<String> errors = listErrors(c);
                List<Elu> correspondances;
                if (errors.isEmpty()) {
                    correspondances =
                        elus
                            .stream()
                            .filter(e -> {
                                if (c == null) {
                                    return false;
                                }
                                if (isDifferent(e.getNom(), c.getNom())) {
                                    return false;
                                }
                                if (isDifferent(e.getPrenom(), c.getPrenom())) {
                                    return false;
                                }
                                if (
                                    e.getDateNaissance() == null ||
                                    !e
                                        .getDateNaissance()
                                        .equals(c.getDateNaissance())
                                ) {
                                    return false;
                                }
                                return true;
                            })
                            .collect(Collectors.toList());
                } else {
                    correspondances = new ArrayList<>();
                }
                if (correspondances.isEmpty()) {
                    return new CandidatCorrespondanceDTO(
                        c,
                        null,
                        false,
                        errors
                    );
                } else if (correspondances.size() == 1) {
                    return new CandidatCorrespondanceDTO(
                        c,
                        correspondances.get(0),
                        false,
                        errors
                    );
                } else {
                    return new CandidatCorrespondanceDTO(c, null, true, errors);
                }
            })
            .collect(Collectors.toList());
        return result;
    }

    private CandidatDTO mapCandidat(CSVRecord record) {
        int cursor = 0;
        Civilite civilite;
        switch (recordGet(record, cursor++)) {
            case "M":
                civilite = Civilite.MONSIEUR;
                break;
            case "Mme":
                civilite = Civilite.MADAME;
                break;
            default:
                civilite = null;
        }
        String nom = recordGet(record, cursor++);
        String prenom = recordGet(record, cursor++);
        LocalDate dateNaissance = null;
        try {
            String d = recordGet(record, cursor++);
            if (d != null) {
                dateNaissance =
                    dateParser
                        .parse(d)
                        .toInstant()
                        .atZone(Constants.parisZoneId)
                        .toLocalDate();
            }
        } catch (ParseException e) {}
        String listeElectorale = recordGet(record, cursor++);
        String listeElectoraleCourt = recordGet(record, cursor++);
        String departement = recordGet(record, cursor++);
        Integer codeDepartement;
        try {
            codeDepartement = Integer.parseInt(recordGet(record, cursor++));
        } catch (NumberFormatException e) {
            codeDepartement = null;
        }
        return new CandidatDTO(
            civilite,
            prenom,
            nom,
            dateNaissance,
            listeElectorale,
            listeElectoraleCourt,
            departement,
            codeDepartement
        );
    }

    private String recordGet(CSVRecord record, Integer index) {
        if (record.size() <= index) {
            return null;
        }
        String r = record.get(index).trim();
        if (r.equals("")) {
            return null;
        }
        return r;
    }

    private List<String> listErrors(CandidatDTO c) {
        List<String> errors = new ArrayList<>();
        if (c.getCivilite() == null) errors.add("civilite");
        if (c.getPrenom() == null) errors.add("prenom");
        if (c.getNom() == null) errors.add("nom");
        if (c.getDateNaissance() == null) errors.add("dateNaissance");
        if (c.getListeElectorale() == null) errors.add("listeElectorale");
        if (c.getListeElectoraleCourt() == null) errors.add(
            "listeElectoraleCourt"
        );
        if (c.getDepartement() == null) errors.add("departement");
        if (c.getCodeDepartement() == null) errors.add("codeDepartement");
        return errors;
    }

    private Boolean isDifferent(String eluNom, String candidatNom) {
        return (
            StringUtils.getJaroWinklerDistance(
                clean(eluNom),
                clean(candidatNom)
            ) <
            0.95
        );
    }

    private String clean(String nom) {
        return StringUtils.stripAccents(nom).trim().toLowerCase();
    }

    private SimpleDateFormat dateParser = new SimpleDateFormat(
        "dd/MM/yyyy",
        Locale.FRENCH
    );

    @Transactional
    public void ajouterElus(
        Long mandatureId,
        List<CandidatCorrespondanceDTO> candidats
    ) {
        Mandature mandature = mandatureRepository.getOne(mandatureId);
        Map<String, ListeElectorale> listeElectoraleMap = new HashMap<>();
        listeElectoraleRepository
            .findByMandature(mandature)
            .forEach(l -> {
                listeElectoraleMap.put(l.getNomCourt() + "///" + l.getNom(), l);
            });
        candidats.forEach(c -> {
            // on le rejoue à dessein
            List<String> errors = listErrors(c.getCandidatDTO());
            if (!errors.isEmpty()) {
                throw new IllegalArgumentException();
            }
        });
        candidats.forEach(c -> {
            String listeElectoralKey =
                c.getCandidatDTO().getListeElectoraleCourt() +
                "///" +
                c.getCandidatDTO().getListeElectorale();
            ListeElectorale listeElectorale = listeElectoraleMap.get(
                listeElectoralKey
            );
            if (listeElectorale == null) {
                ListeElectorale listeElectoraleDraft = new ListeElectorale();
                listeElectoraleDraft.setNom(
                    c.getCandidatDTO().getListeElectorale()
                );
                listeElectoraleDraft.setNomCourt(
                    c.getCandidatDTO().getListeElectoraleCourt()
                );
                listeElectoraleDraft.setMandature(mandature);
                listeElectorale =
                    listeElectoraleRepository.save(listeElectoraleDraft);
                listeElectoraleMap.put(listeElectoralKey, listeElectorale);
            }
            Elu elu = c.getElu();
            if (elu == null) {
                elu = new Elu();
                ShortUid uid = shortUidService.createShortUid();
                elu.setUid(uid.getUid());
                elu.setShortUid(uid.getShortUid());
                elu.setCivilite(c.getCandidatDTO().getCivilite());
                elu.setPrenom(c.getCandidatDTO().getPrenom());
                elu.setNom(c.getCandidatDTO().getNom());
                elu.setDateNaissance(c.getCandidatDTO().getDateNaissance());
                elu = eluRepository.save(elu);
            }
            Mandat mandat = new Mandat();
            mandat.setMandature(mandature);
            mandat.setElu(elu);
            mandat.setListeElectorale(listeElectorale);
            mandat.setDateDebut(mandature.getDateDebut());
            mandat.setDepartement(c.getCandidatDTO().getDepartement());
            mandat.setCodeDepartement(
                c.getCandidatDTO().getCodeDepartement().toString()
            );
            mandatRepository.save(mandat);
        });
    }
}
