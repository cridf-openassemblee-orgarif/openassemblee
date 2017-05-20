package openassemblee.domain;

public class Image {

    private String contentType;
    private byte[] data;

    public Image(String contentType, byte[] data) {
        this.contentType = contentType;
        this.data = data;
    }

    public String getContentType() {
        return contentType;
    }

    public byte[] getData() {
        return data;
    }
}
