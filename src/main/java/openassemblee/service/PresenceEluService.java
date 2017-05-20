package openassemblee.service;

import openassemblee.domain.PresenceElu;
import openassemblee.domain.Signature;
import openassemblee.repository.PresenceEluRepository;
import openassemblee.repository.SignatureRepository;
import openassemblee.repository.search.SignatureSearchRepository;
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
