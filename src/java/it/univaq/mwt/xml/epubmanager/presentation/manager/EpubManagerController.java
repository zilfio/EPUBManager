package it.univaq.mwt.xml.epubmanager.presentation.manager;

import it.univaq.mwt.xml.epubmanager.business.BusinessException;
import it.univaq.mwt.xml.epubmanager.business.impl.EPubManagerServiceImpl;
import it.univaq.mwt.xml.epubmanager.business.model.EpubResource;
import java.io.IOException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/manager")
public class EpubManagerController {
    
    @Autowired
    private EPubManagerServiceImpl service;
    
    @RequestMapping(value="/create",method={RequestMethod.GET})
    public String uploadEpubStart() {
	return "epub.upload";
    }
    
    @RequestMapping(value="/create",method={RequestMethod.POST})
    public String uploadEpub(@RequestParam("epub") MultipartFile epub) throws BusinessException {
	EpubResource resource;
        try {
            byte[] bytes = epub.getBytes();
            resource = new EpubResource(FilenameUtils.getBaseName(epub.getOriginalFilename()), epub.getOriginalFilename(), bytes, epub.getContentType());
            // upload .epub + unzip .epub
            String result = service.addEpub(resource);
            System.out.println("Result: "+result);
        } catch (IOException e) {
            throw new BusinessException("Errore I/O", e);
        }
        
        return "epub.upload.ok";
    }
    
    @RequestMapping(value="/biblio",method={RequestMethod.GET})
    public String biblioStart() {
        
        // operazioni sui file estratti dell'epub
        
        
	return "common.ok";
    }
}
