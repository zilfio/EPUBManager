package it.univaq.mwt.xml.epubmanager.wsdl;

import it.univaq.mwt.soa.epub.MError;
import it.univaq.mwt.soa.epub.TEpubError;
import it.univaq.mwt.xml.epubmanager.business.BusinessException;
import it.univaq.mwt.xml.epubmanager.business.EPubService;
import it.univaq.mwt.xml.epubmanager.business.impl.EPubServiceSOAImpl;
import it.univaq.mwt.xml.epubmanager.business.model.EpubCss;
import it.univaq.mwt.xml.epubmanager.business.model.EpubImage;
import it.univaq.mwt.xml.epubmanager.business.model.EpubXhtml;
import it.univaq.mwt.xml.epubmanager.business.model.Metadata;
import javax.jws.WebService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author zilfio
 */
@WebService(serviceName = "ePub", portName = "ePubSOAPPort", endpointInterface = "it.univaq.mwt.soa.epub.EPubPortType", targetNamespace = "http://www.univaq.it/mwt/soa/ePub.wsdl", wsdlLocation = "WEB-INF/wsdl/EPUBManagerWSDL/ePub.wsdl")
public class EPUBManagerWSDL {
    
    EPubService api = new EPubServiceSOAImpl();
    
    public long startEpub(it.univaq.mwt.soa.epub.TMetadata metadata) throws MError {
        try {
            Metadata m = new Metadata();
            m.setIdentifier(metadata.getIdentifier());
            m.setTitle(metadata.getTitle());
            m.setLanguage(metadata.getLanguage());
            return api.startEpub(m);
        } catch (BusinessException e) {
            TEpubError info = new TEpubError();
            info.setMessage(e.getMessage());
            info.setCode(1000);
            throw new MError("Eccezione", null, e);
        }        
    }

    public int addXhtml(long token, it.univaq.mwt.soa.epub.TEpubXhtml xhtml) throws MError {
        try {
            EpubXhtml epubXhtml = new EpubXhtml();
            epubXhtml.setId(xhtml.getId());
            epubXhtml.setName(xhtml.getName());
            epubXhtml.setPath(xhtml.getPath());
            epubXhtml.setFile(xhtml.getFile());
            epubXhtml.setContentType(xhtml.getContentType());
            epubXhtml.setEpub(xhtml.getEpub());
            epubXhtml.setIndex(xhtml.getIndex());
            epubXhtml.setType(xhtml.getType());
            return api.addXHTML(token, epubXhtml);
        } catch (BusinessException e) {
            TEpubError info = new TEpubError();
            info.setMessage(e.getMessage());
            info.setCode(1000);
            throw new MError("Eccezione", null, e);
        }      
    }

    public int addStylesheet(long token, it.univaq.mwt.soa.epub.TEpubCss css) throws MError {
        try {
            EpubCss epubCss = new EpubCss();
            epubCss.setId(css.getId());
            epubCss.setName(css.getName());
            epubCss.setPath(css.getPath());
            epubCss.setFile(css.getFile());
            epubCss.setContentType(css.getContentType());
            epubCss.setEpub(css.getEpub());
            return api.addStylesheet(token, epubCss);
        } catch (BusinessException e) {
            TEpubError info = new TEpubError();
            info.setMessage(e.getMessage());
            info.setCode(1000);
            throw new MError("Eccezione", null, e);
        } 
    }

    public int addImage(long token, it.univaq.mwt.soa.epub.TEpubImage image) throws MError {
        try {
            EpubImage epubImage = new EpubImage();
            epubImage.setId(image.getId());
            epubImage.setName(image.getName());
            epubImage.setPath(image.getPath());
            epubImage.setFile(image.getFile());
            epubImage.setEpub(image.getEpub());
            epubImage.setContentType(image.getContentType());
            return api.addImage(token, epubImage);
        } catch (BusinessException e) {
            TEpubError info = new TEpubError();
            info.setMessage(e.getMessage());
            info.setCode(1000);
            throw new MError("Eccezione", null, e);
        }
    }

    public void finalizeEpub(long token) throws MError {
        try {
            api.finalizeEpub(token);
        } catch (BusinessException e) {
            TEpubError info = new TEpubError();
            info.setMessage(e.getMessage());
            info.setCode(1000);
            throw new MError("Eccezione", null, e);
        }
    }

    public boolean removeXhtml(long token, it.univaq.mwt.soa.epub.TEpubXhtml xhtml) throws MError {
        try {
            EpubXhtml epubXhtml = new EpubXhtml();
            epubXhtml.setId(xhtml.getId());
            epubXhtml.setName(xhtml.getName());
            epubXhtml.setPath(xhtml.getPath());
            epubXhtml.setFile(xhtml.getFile());
            epubXhtml.setContentType(xhtml.getContentType());
            epubXhtml.setEpub(xhtml.getEpub());
            epubXhtml.setIndex(xhtml.getIndex());
            epubXhtml.setType(xhtml.getType());
            return api.deleteEpubXhtml(epubXhtml);
        } catch (BusinessException e) {
            TEpubError info = new TEpubError();
            info.setMessage(e.getMessage());
            info.setCode(1000);
            throw new MError("Eccezione", null, e);
        }
    }

    public boolean removeStylesheet(long token, it.univaq.mwt.soa.epub.TEpubCss css) throws MError {
        try {
            EpubCss epubCss = new EpubCss();
            epubCss.setId(css.getId());
            epubCss.setName(css.getName());
            epubCss.setPath(css.getPath());
            epubCss.setFile(css.getFile());
            epubCss.setContentType(css.getContentType());
            epubCss.setEpub(css.getEpub());
            return api.deleteEpubCss(epubCss);
        } catch (BusinessException e) {
            TEpubError info = new TEpubError();
            info.setMessage(e.getMessage());
            info.setCode(1000);
            throw new MError("Eccezione", null, e);
        }
    }

    public boolean removeImage(long token, it.univaq.mwt.soa.epub.TEpubImage image) throws MError {
        try {
            EpubImage epubImage = new EpubImage();
            epubImage.setId(image.getId());
            epubImage.setName(image.getName());
            epubImage.setPath(image.getPath());
            epubImage.setFile(image.getFile());
            epubImage.setContentType(image.getContentType());
            epubImage.setEpub(image.getEpub());
            return api.deleteEpubImage(epubImage);
        } catch (BusinessException e) {
            TEpubError info = new TEpubError();
            info.setMessage(e.getMessage());
            info.setCode(1000);
            throw new MError("Eccezione", null, e);
        }
    }
    
}
