package fr.cridf.babylone14166.service;

import fr.cridf.babylone14166.domain.PresenceElu;
import fr.cridf.babylone14166.domain.Signature;
import fr.cridf.babylone14166.repository.PresenceEluRepository;
import fr.cridf.babylone14166.repository.SignatureRepository;
import fr.cridf.babylone14166.repository.search.SignatureSearchRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class PresenceEluService {

    @Inject
    private PresenceEluRepository presenceEluRepository;
    @Inject
    private SignatureRepository signatureRepository;
    @Inject
    private SignatureSearchRepository signatureSearchRepository;

    public void saveSignature(Long id, Signature signature) {
        signatureRepository.save(signature);
        signatureSearchRepository.save(signature);
        PresenceElu presenceElu = presenceEluRepository.getOne(id);
        presenceElu.getSignatures().add(signature);
        presenceEluRepository.save(presenceElu);
    }

    public void updateSignature(Signature signature) {
        signatureRepository.save(signature);
        signatureSearchRepository.save(signature);
    }
}
