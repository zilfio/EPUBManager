package it.univaq.mwt.xml.epubmanager.business;

import it.univaq.mwt.xml.epubmanager.business.model.Epub;
import it.univaq.mwt.xml.epubmanager.business.model.EpubCss;
import it.univaq.mwt.xml.epubmanager.business.model.EpubImage;
import it.univaq.mwt.xml.epubmanager.business.model.EpubXhtml;
import it.univaq.mwt.xml.epubmanager.business.model.Metadata;
import java.util.List;

public interface EPubService {
    
    /**
     * 
     * Avvia una sessione di generazione per un nuovo ebook, i cui metadati sono specificati dalla
     *  struttura passata in input.
     *  @param metadata metadati dell'epub
     *  @return La funzione restituisce un identificatore unico di sessione, che sarà
     *  utilizzato per contestualizzare tutte le chiamate agli altri metodi.
     * @throws BusinessException 
     */
    long startEpub (Metadata metadata) throws BusinessException;   
    
    /**
     * Aggiunge un file XHTML all’ebook specificato dal token. L’ordine di aggiunta di questi file
     * determinerà anche quello indicato dalla spine dell’ePub.
     * @param token rappresenta l'ebook
     * @param epubXhtml rappresenta il file XHTML da aggiungere
     * @return L'intero restituito dovrà rappresentare in maniera univoca l’elemento appena inserito.
     * @throws BusinessException 
     */
    String addXHTML (long token, EpubXhtml epubXhtml) throws BusinessException; 
    
    /**
     * Aggiunge un file CSS (foglio di stile) all’ebook specificato dal token.
     * @param token rappresenta l'ebook
     * @param epubCss rappresenta il file CSS da aggiungere
     * @return  L'intero restituito dovrà rappresentare in maniera univoca l’elemento appena inserito.
     * @throws BusinessException 
     */
    String addStylesheet (long token, EpubCss epubCss) throws BusinessException;
    
    /**
     * Aggiunge un’immagine del tipo specificato all’interno dell’ebook specificato dal token.
     * @param token rappresenta l'ebook
     * @param epubImage rappresenta l'immagine da aggiungere
     * @return L'intero restituito dovrà rappresentare in maniera univoca l’elemento appena inserito.
     * @throws BusinessException 
     */
    String addImage (long token, EpubImage epubImage) throws BusinessException;

    /**
     * Genera l'ebook specificato dal token, dopo aver eventualmente eseguito delle verifiche di
     * correttezza (le stesse che sono eseguite nel progetto ePub Manager prima della generazione
     * finale)
     * @param token
     * @throws BusinessException 
     */
    void finalizeEpub (Epub epub, long token) throws BusinessException;
    
    /**
     * Restituisce l'oggetto che rappresenta il file XHTML dato il suo id
     * @param list
     * @param id
     * @return file xhtml
     */
    EpubXhtml getEpubXhtmlById (List<EpubXhtml> list, String id);
    
    /**
     * Modifica di un oggetto di tipo EpubXhtml
     * @param epub
     * @param epubXhtml 
     */
    void updateEpubXhtml (Epub epub, EpubXhtml epubXhtml);
    
    void deleteEpubXhtml (long token, List<EpubXhtml> list, EpubXhtml epubXhtml);
    
    /**
     * Restituisce l'oggetto che rappresenta il file CSS dato il suo id
     * @param list
     * @param id
     * @return 
     */
    EpubCss getEpubCssById (List<EpubCss> list, String id);
    
    void updateEpubCss (Epub epub, EpubCss epubCss);
    
    void deleteEpubCss (long token, List<EpubCss> list, EpubCss epubCss);
    
    /**
     * Restituisce l'oggetto che rappresenta l'immagine dato il suo id
     * @param list
     * @param id
     * @return 
     */
    EpubImage getEpubImageById (List<EpubImage> list, String id);
    
    void updateEpubImage (Epub epub, EpubImage epubImage);
    
    void deleteEpubImage (long token, List<EpubImage> list, EpubImage epubImage);
}