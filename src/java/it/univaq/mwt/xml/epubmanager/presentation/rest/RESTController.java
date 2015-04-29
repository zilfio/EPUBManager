package it.univaq.mwt.xml.epubmanager.presentation.rest;

import it.univaq.mwt.xml.epubmanager.business.EPubService;
import it.univaq.mwt.xml.epubmanager.business.model.EpubCss;
import it.univaq.mwt.xml.epubmanager.business.model.EpubImage;
import it.univaq.mwt.xml.epubmanager.business.model.EpubXhtml;
import it.univaq.mwt.xml.epubmanager.business.model.Metadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public void finalizeEpub (@PathVariable("token") String epub) {
        long token = Long.parseLong(epub);
        //service.finalizeEpub(token);
    }
    
    @RequestMapping(value="/epubs/{token}/texts",method={RequestMethod.POST})
    @ResponseBody
    public int insertXhtml (@PathVariable("token") String epub, EpubXhtml epubXhtml) {
        long token = Long.parseLong(epub);
        int xhtml = service.addXHTML(token, epubXhtml);
        return xhtml;
    }
    
    
    @RequestMapping(value="/epubs/{token}/texts/{id}",method={RequestMethod.GET})
    @ResponseBody
    public EpubXhtml getXhtml (@PathVariable("id") String id) {
        // long token = Long.parseLong(epub);
        EpubXhtml xhtml = service.getEpubXhtmlByPk(id);
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
    public int insertImage (@PathVariable("token") String epub, EpubImage epubImage) {
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
    public int insertCss (@PathVariable("token") String epub, EpubCss epubCss) {
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
}
