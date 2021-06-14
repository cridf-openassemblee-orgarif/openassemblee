package openassemblee.service;

import openassemblee.domain.Mandature;
import openassemblee.repository.*;
import openassemblee.repository.search.*;
import openassemblee.web.rest.dto.SearchResultDTO;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static openassemblee.service.EluService.isCurrentMandat;
import static openassemblee.web.rest.dto.SearchResultDTO.ResultType.*;

@Service
@Transactional
public class SearchService {

    private final Logger logger = LoggerFactory.getLogger(SearchService.class);

    @Inject
    protected EluRepository eluRepository;
    @Inject
    protected EluSearchRepository eluSearchRepository;

    @Inject
    protected GroupePolitiqueRepository groupePolitiqueRepository;
    @Inject
    protected GroupePolitiqueSearchRepository groupePolitiqueSearchRepository;

    @Inject
    protected OrganismeRepository organismeRepository;
    @Inject
    protected OrganismeSearchRepository organismeSearchRepository;

    @Inject
    protected CommissionThematiqueRepository commissionThematiqueRepository;
    @Inject
    protected CommissionThematiqueSearchRepository commissionThematiqueSearchRepository;

    @Inject
    protected FonctionExecutiveRepository fonctionExecutiveRepository;
    @Inject
    protected FonctionExecutiveSearchRepository fonctionExecutiveSearchRepository;

    @Inject
    protected SessionMandatureService sessionMandatureService;

    public void resetIndex() {
        logger.debug("Reset search index");
        resetRepository(eluRepository, eluSearchRepository);
        resetRepository(groupePolitiqueRepository, groupePolitiqueSearchRepository);
        resetRepository(commissionThematiqueRepository, commissionThematiqueSearchRepository);
        resetRepository(fonctionExecutiveRepository, fonctionExecutiveSearchRepository);
    }

    private <T> void resetRepository(JpaRepository<T, Long> jpaRepository,
                                     ElasticsearchRepository<T, Long> elasticsearchRepository) {
        elasticsearchRepository.deleteAll();
        for (T t : jpaRepository.findAll()) {
            elasticsearchRepository.save(t);
        }
    }

    @Transactional(readOnly = true)
    public List<SearchResultDTO> search(String searchToken) {
        // non ! je n'ai pas trouvé comment ajouter un filter pour les accents au moment de l'indexation...
        // QueryStringQueryBuilder qb = new QueryStringQueryBuilder(StringUtils.stripAccents(searchToken) + "*");
        Mandature mandature = sessionMandatureService.getMandature();
        QueryStringQueryBuilder qb = new QueryStringQueryBuilder(searchToken + "*");
        List<SearchResultDTO> results = new ArrayList<>();
        results.addAll(StreamSupport
            .stream(eluSearchRepository.search(qb).spliterator(), false)
            // eluRepository.getOne car elu retourné par searchRepo ne branche pas les mandats
            .filter(e -> isCurrentMandat(eluRepository.getOne(e.getId()).getMandats(), mandature))
            .map(e -> new SearchResultDTO(ELU, e.getId(), e.civiliteComplete(), e.getImage()))
            .collect(Collectors.toList()));
        if (results.size() < 20) {
            results.addAll(StreamSupport
                .stream(groupePolitiqueSearchRepository.search(qb).spliterator(), false)
                .filter(gp -> gp.getMandature().getId().equals(mandature.getId()))
                .map(e -> new SearchResultDTO(GROUPE_POLITIQUE, e.getId(), e.getNom(), e.getImage()))
                .collect(Collectors.toList()));
            if (results.size() < 20) {
                results.addAll(StreamSupport
                    .stream(commissionThematiqueSearchRepository.search(qb).spliterator(), false)
                    .filter(ct -> ct.getMandature().getId().equals(mandature.getId()))
                    .map(e -> new SearchResultDTO(COMMISSION_THEMATIQUE, e.getId(), e.getNom(), null))
                    .collect(Collectors.toList()));
                if (results.size() < 20) {
                    results.addAll(StreamSupport
                        .stream(fonctionExecutiveSearchRepository.search(qb).spliterator(), false)
                        .map(f -> eluRepository.getOne(f.getElu().getId()))
                        .filter(e -> isCurrentMandat(e.getMandats(), mandature))
                        .map(e -> new SearchResultDTO(ELU, e.getId(), e.civiliteComplete(), e.getImage()))
                        .collect(Collectors.toList()));
                }
            }
        }
        if (results.size() > 20) {
            results = results.subList(0, 20);
        }
        return results;
    }

}
