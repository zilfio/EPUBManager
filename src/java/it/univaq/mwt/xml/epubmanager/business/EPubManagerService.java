package it.univaq.mwt.xml.epubmanager.business;

import it.univaq.mwt.xml.epubmanager.business.model.EpubResource;

public interface EPubManagerService {
    
    /**
     * Aggiunge un epub nel repository interno e lo decomprime
     * @param epubResource
     * @return
     * @throws BusinessException 
     */
    String addEpub (EpubResource epubResource) throws BusinessException;
    
}
