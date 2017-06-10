package openassemblee.repository;

import openassemblee.domain.Image;
import org.elasticsearch.common.io.Streams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.StreamUtils;

import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

@Repository
public class ImageRepository {

    private final Logger log = LoggerFactory.getLogger(ImageRepository.class);

    private static final String INSERT_IMAGE = "insert into images(content_type, data) values (?, ?)";
    private static final String READ_IMAGE = "select content_type, data from images where id = ?";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    private void init(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Long saveImage(Image image) throws SQLException {
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT_IMAGE, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, image.getContentType());
            ps.setBytes(2, image.getData());
            return ps;
        }, holder);
        return holder.getKey().longValue();
    }

    public Image getImage(Long id) throws SQLException, IOException {
        try {
            return jdbcTemplate.queryForObject(READ_IMAGE, new Long[] { id }, (rs, rowNum) -> {
                String contentType = rs.getString("content_type");
                Blob blob = rs.getBlob("data");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                try {
                    StreamUtils.copy(blob.getBinaryStream(), baos);
                } catch (IOException e) {
                    // TODO exception
                    throw new RuntimeException(e);
                }
                return new Image(contentType, baos.toByteArray());
            });
        } catch (EmptyResultDataAccessException e) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Streams.copy(getClass().getClassLoader().getResourceAsStream("pas-dimage.jpg"), baos);
            return new Image("image/jpeg", baos.toByteArray());
        }
    }

}
