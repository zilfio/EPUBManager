package it.univaq.mwt.xml.epubmanager.business.impl;

import it.univaq.mwt.xml.epubmanager.business.BusinessException;
import it.univaq.mwt.xml.epubmanager.business.EPubManagerService;
import it.univaq.mwt.xml.epubmanager.business.model.EpubResource;
import it.univaq.mwt.xml.epubmanager.common.utility.ZipUtil;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EPubManagerServiceImpl implements EPubManagerService {

    @Value("#{cfgproperties.uploadEpubDirectory}")
    private String UPLOAD_EPUB_DIR;

    @Override
    public String addEpub(EpubResource epubResource) throws BusinessException {

        if (epubResource.getId() != null) {
            //creating a temp file
            File temp = new File(UPLOAD_EPUB_DIR + epubResource.getPath());
            try {
                //saving image to file
                FileUtils.writeByteArrayToFile(temp, epubResource.getFile());
                System.out.println("Upload del file " + epubResource.getPath() + " avvenuto con successo!");
            } catch (IOException ex) {
                throw new BusinessException("Upload del file " + epubResource.getPath() + " fallito!", ex);
            }
            
            // scompatto il file .epub nel repository
            ZipUtil.unzipArchive(UPLOAD_EPUB_DIR, UPLOAD_EPUB_DIR + epubResource.getPath());
            
            return epubResource.getId();
        } else {
            return null;
        }

    }
}