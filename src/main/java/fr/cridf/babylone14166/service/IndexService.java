package fr.cridf.babylone14166.service;

import static fr.cridf.babylone14166.web.rest.dto.SearchResultDTO.ResultType.COMMISSION_THEMATIQUE;
import static fr.cridf.babylone14166.web.rest.dto.SearchResultDTO.ResultType.ELU;
import static fr.cridf.babylone14166.web.rest.dto.SearchResultDTO.ResultType.GROUPE_POLITIQUE;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.springframework.data.elasticsearch.annotations.FieldType.Auto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import fr.cridf.babylone14166.web.rest.dto.SearchResultDTO;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import fr.cridf.babylone14166.repository.*;
import fr.cridf.babylone14166.repository.search.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

@Service
@Transactional
public class IndexService {

    private final Logger logger = LoggerFactory.getLogger(IndexService.class);

    @Autowired
    protected EluRepository eluRepository;
    @Autowired
    protected EluSearchRepository eluSearchRepository;

    @Autowired
    protected GroupePolitiqueRepository groupePolitiqueRepository;
    @Autowired
    protected GroupePolitiqueSearchRepository groupePolitiqueSearchRepository;

    @Autowired
    protected OrganismeRepository organismeRepository;
    @Autowired
    protected OrganismeSearchRepository organismeSearchRepository;

    @Autowired
    protected CommissionThematiqueRepository commissionThematiqueRepository;
    @Autowired
    protected CommissionThematiqueSearchRepository commissionThematiqueSearchRepository;

    @Autowired
    protected FonctionExecutiveRepository fonctionExecutiveRepository;
    @Autowired
    protected FonctionExecutiveSearchRepository fonctionExecutiveSearchRepository;

    public void resetIndex() {
        logger.debug("Reset search index");
        resetRepository(eluRepository, eluSearchRepository);
        resetRepository(groupePolitiqueRepository, groupePolitiqueSearchRepository);
        resetRepository(organismeRepository, organismeSearchRepository);
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

    public List<SearchResultDTO> search(String searchToken) {
        QueryStringQueryBuilder qb = new QueryStringQueryBuilder(searchToken + "*");
        List<SearchResultDTO> results = new ArrayList<>();
        results.addAll(StreamSupport
            .stream(eluSearchRepository.search(qb).spliterator(), false)
            .map(e -> new SearchResultDTO(ELU, e.getId(), e.civiliteComplete(), e.getImage()))
            .collect(Collectors.toList()));
        if(results.size() < 20 ) {
            results.addAll(StreamSupport
                .stream(groupePolitiqueSearchRepository.search(qb).spliterator(), false)
                .map(e -> new SearchResultDTO(GROUPE_POLITIQUE, e.getId(), e.getNom(), e.getImage()))
                .collect(Collectors.toList()));
            if (results.size() < 20) {
                results.addAll(StreamSupport
                    .stream(commissionThematiqueSearchRepository.search(qb).spliterator(), false)
                    .map(e -> new SearchResultDTO(COMMISSION_THEMATIQUE, e.getId(), e.getNom(), null))
                    .collect(Collectors.toList()));
                if (results.size() < 20) {
                    results.addAll(StreamSupport
                        .stream(fonctionExecutiveSearchRepository.search(qb).spliterator(), false)
                        .map(f -> eluRepository.getOne(f.getElu().getId()))
                        .map(e -> new SearchResultDTO(ELU, e.getId(), e.civiliteComplete(), e.getImage()))
                        .collect(Collectors.toList()));
                }
            }
        }
        if(results.size() > 20 ) {
            results = results.subList(0, 20);
        }
        return results;
    }

}
