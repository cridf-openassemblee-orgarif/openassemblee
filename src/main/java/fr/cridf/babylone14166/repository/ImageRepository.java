package fr.cridf.babylone14166.repository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.*;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.elasticsearch.common.io.Streams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.cridf.babylone14166.domain.Image;

@Repository
public class ImageRepository {

    private final Logger log = LoggerFactory.getLogger(ImageRepository.class);

    private static final String INSERT_IMAGE = "insert into images(content_type, data) values (?, ?)";
    private static final String READ_IMAGE = "select content_type, data from images where id = ?";

    @Autowired
    protected DataSource dataSource;

    protected Connection connection;

    @PostConstruct
    public void init() {
        try {
            // TODO la création d'une nouvelle connexion est un probleme
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            log.error("Impossible de créer la connexion à la base", e);
        }
    }

    public Long saveImage(Image image) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_IMAGE,
            Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, image.getContentType());
        preparedStatement.setBytes(2, image.getData());
        preparedStatement.executeUpdate();
        ResultSet keys = preparedStatement.getGeneratedKeys();
        if (keys.next()) {
            return keys.getLong(1);
        }
        return null;
    }

    public Image getImage(Long id) throws SQLException, IOException {
        PreparedStatement preparedStatement = connection.prepareStatement(READ_IMAGE);
        preparedStatement.setLong(1, id);
        ResultSet result = preparedStatement.executeQuery();
        while (result.next()) {
            String contentType = result.getString("content_type");
            Blob blob = result.getBlob("data");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Streams.copy(blob.getBinaryStream(), baos);
            return new Image(contentType, baos.toByteArray());
        }
        // TODO utiliser l'image 'pas d'image'
        return null;
    }

}
