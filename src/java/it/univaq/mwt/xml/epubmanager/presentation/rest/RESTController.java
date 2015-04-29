package it.univaq.mwt.xml.epubmanager.presentation.rest;

import it.univaq.mwt.xml.epubmanager.business.EPubService;
import it.univaq.mwt.xml.epubmanager.business.model.EpubCss;
import it.univaq.mwt.xml.epubmanager.business.model.EpubImage;
import it.univaq.mwt.xml.epubmanager.business.model.EpubXhtml;
import it.univaq.mwt.xml.epubmanager.business.model.Metadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author zilfio
 */

@Controller
@RequestMapping("/rest")
public class RESTController {
    
    @Autowired
    private EPubService service;
    
    @RequestMapping(value="/epubs",method={RequestMethod.POST})
    @ResponseBody
    public String startEpub (Metadata metadata) {
        long result = service.startEpub(metadata);
        return Long.toString(result);
    }
    
    @RequestMapping(value="/epubs/{token}",method={RequestMethod.GET})
    @ResponseBody
    public String finalizeEpub (@PathVariable("token") String epub) {
        long token = Long.parseLong(epub);
        return Long.toString(token);
        //service.finalizeEpub(token);
    }
    
    @RequestMapping(value="/epubs/{token}/texts",method={RequestMethod.POST})
    @ResponseBody
    public int insertXhtml (@PathVariable("token") String epub, @RequestBody EpubXhtml epubXhtml) {
        long token = Long.parseLong(epub);
        int xhtml = service.addXHTML(token, epubXhtml);
        return xhtml;
    }
    
    
    @RequestMapping(value="/epubs/{token}/texts/{id}",method={RequestMethod.GET})
    @ResponseBody
    public EpubXhtml getXhtml (@PathVariable("id") String id) {
        // long token = Long.parseLong(epub);
        EpubXhtml xhtml = service.getEpubXhtmlByPk(id);
        if (xhtml == null) {
            throw new NotFoundException();
        }
        return xhtml;
    }
    
    @RequestMapping(value="/epubs/{token}/texts/{id}",method={RequestMethod.DELETE})
    @ResponseBody
    public void deleteXhtml (@PathVariable("token") String epub, @PathVariable("id") String id) {
        // long token = Long.parseLong(epub);
        EpubXhtml xhtml = service.getEpubXhtmlByPk(id);
        service.deleteEpubXhtml(xhtml);
    }
    
    @RequestMapping(value="/epubs/{token}/images",method={RequestMethod.POST})
    @ResponseBody
    public int insertImage (@PathVariable("token") String epub, @RequestBody EpubImage epubImage) {
        long token = Long.parseLong(epub);
        int image = service.addImage(token, epubImage);
        return image;
    }
    
    
    @RequestMapping(value="/epubs/{token}/images/{id}",method={RequestMethod.GET})
    @ResponseBody
    public EpubImage getImage (@PathVariable("id") String id) {
        // long token = Long.parseLong(epub);
        EpubImage image = service.getEpubImageByPk(id);
        return image;
    }
    
    @RequestMapping(value="/epubs/{token}/images/{id}",method={RequestMethod.DELETE})
    @ResponseBody
    public void deleteImage (@PathVariable("token") String epub, @PathVariable("id") String id, EpubImage epubImage) {
        // long token = Long.parseLong(epub);
        service.deleteEpubImage(epubImage);
    }
    
    @RequestMapping(value="/epubs/{token}/stylesheets",method={RequestMethod.POST})
    @ResponseBody
    public int insertCss (@PathVariable("token") String epub, @RequestBody EpubCss epubCss) {
        long token = Long.parseLong(epub);
        int css = service.addStylesheet(token, epubCss);
        return css;
    }
    
    
    @RequestMapping(value="/epubs/{token}/stylesheets/{id}",method={RequestMethod.GET})
    @ResponseBody
    public EpubCss getCss (@PathVariable("id") String id) {
        // long token = Long.parseLong(epub);
        EpubCss css = service.getEpubCssByPk(id);
        return css;
    }
    
    @RequestMapping(value="/epubs/{token}/stylesheets/{id}",method={RequestMethod.DELETE})
    @ResponseBody
    public void deleteCss (@PathVariable("token") String epub, @PathVariable("id") String id, EpubCss epubCss) {
        // long token = Long.parseLong(epub);
        service.deleteEpubCss(epubCss);
    }
    
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Not found")
    void handleNotFound(NotFoundException exc) {
    }

    class NotFoundException extends RuntimeException {
        private static final long serialVersionUID = 1L;
    }
}
