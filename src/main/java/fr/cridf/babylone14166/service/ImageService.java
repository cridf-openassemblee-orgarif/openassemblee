package fr.cridf.babylone14166.service;

import java.sql.SQLException;
import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.cridf.babylone14166.domain.*;
import fr.cridf.babylone14166.repository.*;

@Service
@Transactional
public class ImageService {

    @Inject
    private ImageRepository imageRepository;

    @Inject
    private EluRepository eluRepository;

    @Inject
    private GroupePolitiqueRepository groupePolitiqueRepository;

    public void saveImagePourGroupePolitique(Long groupePolitiqueId, Image image) throws SQLException {
        Long imageId = imageRepository.saveImage(image);
        // TODO un truc plus clean...
        GroupePolitique groupePolitique = groupePolitiqueRepository.findOne(groupePolitiqueId);
        groupePolitique.setImage(imageId);
        groupePolitiqueRepository.save(groupePolitique);
    }

    public void saveImagePourElu(Long eluId, Image image) throws SQLException {
        Long imageId = imageRepository.saveImage(image);
        // TODO un truc plus clean...
        Elu elu = eluRepository.findOne(eluId);
        elu.setImage(imageId);
        eluRepository.save(elu);
    }

}
