package fr.cridf.babylone14166.repository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.*;

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
    protected Connection connection;

    public Long saveImage(byte[] imageData) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_IMAGE);
        preparedStatement.setBytes(1, imageData);
        return preparedStatement.executeLargeUpdate();
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
