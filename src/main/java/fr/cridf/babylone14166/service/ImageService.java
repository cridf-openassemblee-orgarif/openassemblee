package fr.cridf.babylone14166.service;

import fr.cridf.babylone14166.domain.Elu;
import fr.cridf.babylone14166.domain.GroupePolitique;
import fr.cridf.babylone14166.domain.Image;
import fr.cridf.babylone14166.repository.EluRepository;
import fr.cridf.babylone14166.repository.GroupePolitiqueRepository;
import fr.cridf.babylone14166.repository.ImageRepository;
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

    @Transactional
    public void saveImagePourGroupePolitique(Long groupePolitiqueId, Image image) throws SQLException {
        Long imageId = imageRepository.saveImage(image);
        // TODO un truc plus clean...
        GroupePolitique groupePolitique = groupePolitiqueRepository.findOne(groupePolitiqueId);
        groupePolitique.setImage(imageId);
        groupePolitiqueRepository.save(groupePolitique);
    }

    @Transactional
    public void saveImagePourElu(Long eluId, Image image) throws SQLException {
        Long imageId = imageRepository.saveImage(image);
        // TODO un truc plus clean...
        Elu elu = eluRepository.findOne(eluId);
        elu.setImage(imageId);
        eluRepository.save(elu);
    }

}
