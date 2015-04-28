package it.univaq.mwt.xml.epubmanager.business.model;

import java.io.Serializable;

public class EpubImage extends EpubResource implements Serializable {

    public EpubImage() {
    }
    
    public EpubImage(String id, String name, String path, byte[] file, String contentType) {
        super(id, name, path, file, contentType);
    }

    public EpubImage(String name, String path, byte[] file, String contentType) {
        super(name, path, file, contentType);
    }

    @Override
    public String toString() {
        return "EpubImage{" + "id=" + super.getId() + ", name=" + super.getName() + ", path=" + super.getPath() + ", file=" + super.getFile() + '}';
    }
}