package fr.cridf.babylone14166.domain;

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

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
