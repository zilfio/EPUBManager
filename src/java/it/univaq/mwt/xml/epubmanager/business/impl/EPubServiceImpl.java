package it.univaq.mwt.xml.epubmanager.business.impl;

import it.univaq.mwt.xml.epubmanager.business.BusinessException;
import it.univaq.mwt.xml.epubmanager.business.EPubService;
import it.univaq.mwt.xml.epubmanager.business.StAXService;
import it.univaq.mwt.xml.epubmanager.business.model.Epub;
import it.univaq.mwt.xml.epubmanager.business.model.EpubCss;
import it.univaq.mwt.xml.epubmanager.business.model.EpubImage;
import it.univaq.mwt.xml.epubmanager.business.model.EpubXhtml;
import it.univaq.mwt.xml.epubmanager.business.model.Metadata;
import it.univaq.mwt.xml.epubmanager.common.utility.Config;
import it.univaq.mwt.xml.epubmanager.common.utility.DirectoryUtil;
import it.univaq.mwt.xml.epubmanager.common.utility.XMLUtil;
import it.univaq.mwt.xml.epubmanager.common.utility.ZipUtil;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EPubServiceImpl implements EPubService{

    @Autowired
    private StAXService StAXService;
    
    private static final String UPLOAD_DIR = Config.getSetting("uploadDirectory");
    
    @Override
    public long startEpub(Metadata metadata) throws BusinessException {
        
        System.out.println("Business logic: startEpub");
        
        long result = Long.parseLong(metadata.getIdentifier());
        
        // creo una cartella con l'isbn dell'epub
        DirectoryUtil.mkdir(UPLOAD_DIR, metadata.getIdentifier());
        
        return result;
    }

    @Override
    public String addXHTML(long token, EpubXhtml epubXhtml) throws BusinessException {
        System.out.println("TOKEN: "+token);
        
        if (epubXhtml.getId() != null) {
            //creating a temp file
            File temp = new File(UPLOAD_DIR + Long.toString(token) + File.separator + epubXhtml.getPath());
            try {
                //saving image to file
                FileUtils.writeByteArrayToFile(temp, epubXhtml.getFile());
                System.out.println("Upload dei file " + epubXhtml.getPath()+ " avvenuto con successo!");
            } catch (IOException ex) {
                throw new BusinessException("Upload del file " + epubXhtml.getPath() + " fallito!", ex);
            }

            // controllo che il file sia valido
            boolean b = XMLUtil.validateXhtml(temp, true);
            System.out.println("ERRORI NELLA VALIDAZIONE? "+b);

            // cancello il file se ho errori di validazione
            /*if(b == true) {
                System.out.println("Cancello il file uplodato: non è valido!");
                temp.delete();
            }*/

            return epubXhtml.getId();
        } else {
            return null;
        }
    }

    @Override
    public String addStylesheet(long token, EpubCss epubCss) throws BusinessException {
        System.out.println("TOKEN: "+token);
        
        if (epubCss.getId() != null) {
            //creating a temp file
            File temp = new File(UPLOAD_DIR + Long.toString(token) + File.separator + epubCss.getPath());
            try {
                //saving image to file
                FileUtils.writeByteArrayToFile(temp, epubCss.getFile());
                System.out.println("Upload del file " + epubCss.getPath()+ " avvenuto con successo!");
            } catch (IOException ex) {
                throw new BusinessException("Upload del file " + epubCss.getPath() + " fallito!", ex);
            }
            //getting image format
            //format =  ImageUtil.getFormat(temp);
            return epubCss.getId();
        } else {
            return null;
        }
    }

    @Override
    public String addImage(long token, EpubImage epubImage) throws BusinessException {
        System.out.println("TOKEN: "+token);
        
        if (epubImage.getId() != null) {
            //creating a temp file
            File temp = new File(UPLOAD_DIR + Long.toString(token) + File.separator + epubImage.getPath());
            try {
                //saving image to file
                FileUtils.writeByteArrayToFile(temp, epubImage.getFile());
                System.out.println("Upload del file " + epubImage.getPath()+ " avvenuto con successo!");
            } catch (IOException ex) {
                throw new BusinessException("Upload del file " + epubImage.getPath() + " fallito!", ex);
            }
            //getting image format
            //format =  ImageUtil.getFormat(temp);
            return epubImage.getId();
        } else {
            return null;
        }
        
    }

    @Override
    public void finalizeEpub(Epub epub, long token) throws BusinessException {
        
        String upload = UPLOAD_DIR + Long.toString(token) + File.separator;

        // creo la cartella META-INF
        DirectoryUtil.mkdir(upload, "META-INF");

        // creo il file toc.ncx
        StAXService.createTocNcxXMLFile(epub, upload);

        // creo il file content.opf
        StAXService.createContentXMLFile(epub, upload);

        // creo il file container.xml
        StAXService.createContainerXMLFile(upload + "META-INF" + File.separator);
        
        String uploadZip = UPLOAD_DIR + Long.toString(token) + File.separator;
        ZipUtil.createArchiveFile(uploadZip, Long.toString(token));
    }

    @Override
    public EpubXhtml getEpubXhtmlById(List<EpubXhtml> list, String id) {
        for (EpubXhtml epubXhtml : list) {
            if(epubXhtml.getId().equals(id)) {
                return epubXhtml;
            }
        }
        return null;
    }

    @Override
    public void updateEpubXhtml(Epub epub, EpubXhtml epubXhtml) {
        try {
            EpubXhtml e = getEpubXhtmlById(epub.getEpubXhtmls(), epubXhtml.getId());
            BeanUtils.copyProperties(e, epubXhtml);
        } catch (IllegalAccessException ex) {
            throw new BusinessException("IllegalAccessException", ex);
        } catch (InvocationTargetException ex) {
            throw new BusinessException("InvocationTargetException", ex);
        }
    }

    @Override
    public void deleteEpubXhtml(long token, List<EpubXhtml> list, EpubXhtml epubXhtml) {
        Iterator<EpubXhtml> i = list.iterator();
        while (i.hasNext()) {
            EpubXhtml xhtml = i.next();
            if(xhtml.getId().equals(epubXhtml.getId())) {
                // rimuovo il file dal file system
                File file = new File(UPLOAD_DIR + Long.toString(token) + File.separator + epubXhtml.getPath());
                boolean delete = file.delete();
                if(delete == true) {
                    // rimuovo il file dalla lista
                    i.remove();
                }
            }
        }
    }

    @Override
    public EpubCss getEpubCssById(List<EpubCss> list, String id) {
        for (EpubCss epubCss : list) {
            if(epubCss.getId().equals(id)) {
                return epubCss;
            }
        }
        return null;
    }

    @Override
    public void updateEpubCss(Epub epub, EpubCss epubCss) {
        try {
            EpubCss e = getEpubCssById(epub.getEpubCsss(), epubCss.getId());
            BeanUtils.copyProperties(e, epubCss);
        } catch (IllegalAccessException ex) {
            throw new BusinessException("IllegalAccessException", ex);
        } catch (InvocationTargetException ex) {
            throw new BusinessException("InvocationTargetException", ex);
        }
    }

    @Override
    public void deleteEpubCss(long token, List<EpubCss> list, EpubCss epubCss) {
        Iterator<EpubCss> i = list.iterator();
        while (i.hasNext()) {
            EpubCss css = i.next();
            if(css.getId().equals(epubCss.getId())) {
                // rimuovo il file dal file system
                File file = new File(UPLOAD_DIR + Long.toString(token) + File.separator + epubCss.getPath());
                boolean delete = file.delete();
                if(delete == true) {
                    // rimuovo il file dalla lista
                    i.remove();
                }
            }
        }
    }

    @Override
    public EpubImage getEpubImageById(List<EpubImage> list, String id) {
        for (EpubImage epubImage : list) {
            if(epubImage.getId().equals(id)) {
                return epubImage;
            }
        }
        return null;
    }

    @Override
    public void updateEpubImage(Epub epub, EpubImage epubImage) {
        try {
            EpubImage e = getEpubImageById(epub.getEpubImages(), epubImage.getId());
            BeanUtils.copyProperties(e, epubImage);
        } catch (IllegalAccessException ex) {
            throw new BusinessException("IllegalAccessException", ex);
        } catch (InvocationTargetException ex) {
            throw new BusinessException("InvocationTargetException", ex);
        }
    }

    @Override
    public void deleteEpubImage(long token, List<EpubImage> list, EpubImage epubImage) {
        Iterator<EpubImage> i = list.iterator();
        while (i.hasNext()) {
            EpubImage image = i.next();
            if(image.getId().equals(epubImage.getId())) {
                // rimuovo il file dal file system
                File file = new File(UPLOAD_DIR + Long.toString(token) + File.separator + epubImage.getPath());
                boolean delete = file.delete();
                if(delete == true) {
                    // rimuovo il file dalla lista
                    i.remove();
                }
            }
        }
    }
     
}