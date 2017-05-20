package openassemblee.service;

import openassemblee.domain.Elu;
import openassemblee.domain.GroupePolitique;
import openassemblee.domain.Image;
import openassemblee.repository.EluRepository;
import openassemblee.repository.GroupePolitiqueRepository;
import openassemblee.repository.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.sql.SQLException;

@Service
public class ImageService {

    @Inject
    private ImageRepository imageRepository;

    @Inject
    private EluRepository eluRepository;

    @Inject
    private GroupePolitiqueRepository groupePolitiqueRepository;

    @Inject
    private AuditTrailService auditTrailService;

    @Transactional
    public void saveImagePourGroupePolitique(Long groupePolitiqueId, Image image) throws SQLException {
        Long imageId = imageRepository.saveImage(image);
        // TODO un truc plus clean...
        GroupePolitique groupePolitique = groupePolitiqueRepository.findOne(groupePolitiqueId);
        groupePolitique.setImage(imageId);
        groupePolitiqueRepository.save(groupePolitique);
        auditTrailService.logUpdate(groupePolitique, groupePolitique.getId());
    }

    @Transactional
    public void saveImagePourElu(Long eluId, Image image) throws SQLException {
        Long imageId = imageRepository.saveImage(image);
        // TODO un truc plus clean...
        Elu elu = eluRepository.findOne(eluId);
        elu.setImage(imageId);
        eluRepository.save(elu);
        auditTrailService.logUpdate(elu, elu.getId());
    }

}
