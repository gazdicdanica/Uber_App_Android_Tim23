package com.example.uberapp_tim.model.vehicle;

public class Document {
    private Long id;
    private String name;
    private String documentImage;
    private Long driverId;

    @Override
    public String toString() {
        return "Document{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", documentImage='" + documentImage + '\'' +
                ", driverId=" + driverId +
                '}';
    }

    public Document() { }

    public Document(String name, String documentImage, Long driverId) {
        this.name = name;
        this.documentImage = documentImage;
        this.driverId = driverId;
    }

    public Document(Long id, String name, String documentImage, Long driverId) {
        this.id = id;
        this.name = name;
        this.documentImage = documentImage;
        this.driverId = driverId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDocumentImage() {
        return documentImage;
    }

    public void setDocumentImage(String documentImage) {
        this.documentImage = documentImage;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }
}
