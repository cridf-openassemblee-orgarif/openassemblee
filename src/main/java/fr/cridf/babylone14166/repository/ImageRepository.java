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

@Repository
public class ImageRepository {

    private final Logger log = LoggerFactory.getLogger(ImageRepository.class);

    private static final String INSERT_IMAGE = "insert into images(data) values (?)";
    private static final String READ_IMAGE = "select data from images where id = ?";

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

    public Long saveImage(byte[] imageData) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_IMAGE,
            Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setBytes(1, imageData);
        preparedStatement.executeUpdate();
        ResultSet keys = preparedStatement.getGeneratedKeys();
        if (keys.next()) {
            return keys.getLong(1);
        }
        return null;
    }

    public byte[] getImage(Long id) throws SQLException, IOException {
        PreparedStatement preparedStatement = connection.prepareStatement(READ_IMAGE);
        preparedStatement.setLong(1, id);
        ResultSet result = preparedStatement.executeQuery();
        while (result.next()) {
            Blob blob = result.getBlob("data");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Streams.copy(blob.getBinaryStream(), baos);
            return baos.toByteArray();
        }
        // TODO utiliser l'image 'pas d'image'
        return null;
    }

}
