package it.univaq.mwt.xml.epubmanager.presentation.packager;

import it.univaq.mwt.xml.epubmanager.business.BusinessException;
import it.univaq.mwt.xml.epubmanager.business.EPubService;
import it.univaq.mwt.xml.epubmanager.business.model.EpubCss;
import it.univaq.mwt.xml.epubmanager.business.model.EpubImage;
import it.univaq.mwt.xml.epubmanager.business.model.EpubResource;
import it.univaq.mwt.xml.epubmanager.business.model.EpubXhtml;
import it.univaq.mwt.xml.epubmanager.business.model.Metadata;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/packager")
public class EpubPackagerController {

    @Value("#{cfgproperties.uploadDirectory}")
    private String UPLOAD_DIR;
    
    @Autowired
    private EPubService service;

    @Autowired
    private FormMetadataValidator metadatavalidator;

    @RequestMapping(value = "/create", method = {RequestMethod.GET})
    public String createMetadataStart(Model model) {

        model.addAttribute("metadata", new Metadata());
        return "metadata.createform";
    }

    @RequestMapping(value = "/create", method = {RequestMethod.POST})
    public String createMetadata(@ModelAttribute Metadata metadata, BindingResult bindingResult) throws BusinessException {
        metadatavalidator.validate(metadata, bindingResult);
        if (bindingResult.hasErrors()) {
            return "metadata.createform";
        }
        long id = service.startEpub(metadata);
        System.out.println("ID univoco per le altre chiamate: " + id);
        return "redirect:/packager/uploadresources?epub=" + id;
    }

    @RequestMapping(value = "/uploadresources", method = {RequestMethod.GET})
    public String uploadResourcesStart(@RequestParam("epub") String epub, Model model) {
        model.addAttribute("epub", epub);
        return "uploadresources.createform";
    }

    @RequestMapping(value = "/uploadresources", method = {RequestMethod.POST})
    public String uploadResources(@RequestParam("epub") String epub,
            @RequestParam("xhtmlfiles") MultipartFile[] xhtmlfiles,
            @RequestParam("cssfiles") MultipartFile[] cssfiles,
            @RequestParam("imagefiles") MultipartFile[] imagefiles) throws BusinessException {

        // risorse uplodate con successo
        List<EpubResource> resourcesUpload = new ArrayList<EpubResource>();
        // risorse non uplodate
        List<EpubResource> resourcesNotUpload = new ArrayList<EpubResource>();

        // mantine l'ordine di inserimento dei file xhtml
        int order = 0;

        // gestione upload dei file XHTML
        for (MultipartFile xhtmlfile : xhtmlfiles) {
            if (!xhtmlfile.isEmpty()) {
                try {
                    byte[] bytes = xhtmlfile.getBytes();
                    EpubXhtml epubXhtml = new EpubXhtml(null, FilenameUtils.getBaseName(xhtmlfile.getOriginalFilename()), xhtmlfile.getOriginalFilename(), bytes, ++order, "application/xhtml+xml", epub, null);
                    int result = service.addXHTML(Long.parseLong(epub), epubXhtml);
                    if (result != 0) {
                        resourcesUpload.add(epubXhtml);
                    } else {
                        resourcesNotUpload.add(epubXhtml);
                    }
                } catch (IOException e) {
                    throw new BusinessException("Errore I/O", e);
                }
            }
        }

        // gestione upload dei file CSS
        for (MultipartFile cssfile : cssfiles) {
            if (!cssfile.isEmpty()) {
                try {
                    byte[] bytes = cssfile.getBytes();
                    EpubCss epubCss = new EpubCss(null, FilenameUtils.getBaseName(cssfile.getOriginalFilename()), cssfile.getOriginalFilename(), bytes, cssfile.getContentType(), epub);
                    System.out.println(epubCss.toString());
                    int result = service.addStylesheet(Long.parseLong(epub), epubCss);
                    if (result != 0) {
                        resourcesUpload.add(epubCss);
                    } else {
                        resourcesNotUpload.add(epubCss);
                    }
                } catch (IOException e) {
                    throw new BusinessException("Errore I/O", e);
                }
            }
        }

        // gestione upload delle immagini
        for (MultipartFile imagefile : imagefiles) {
            if (!imagefile.isEmpty()) {
                try {
                    byte[] bytes = imagefile.getBytes();
                    EpubImage epubImage = new EpubImage(null, FilenameUtils.getBaseName(imagefile.getOriginalFilename()), imagefile.getOriginalFilename(), bytes, imagefile.getContentType(), epub);
                    System.out.println(epubImage.toString());
                    int result = service.addImage(Long.parseLong(epub), epubImage);
                    if (result != 0) {
                        resourcesUpload.add(epubImage);
                    } else {
                        resourcesNotUpload.add(epubImage);
                    }
                } catch (IOException e) {
                    throw new BusinessException("Errore I/O", e);
                }
            }
        }

        return "redirect:/packager/orderresources?epub=" + epub;

    }

    @RequestMapping(value = "/orderresources", method = {RequestMethod.GET})
    public String orderResourcesStart(@RequestParam("epub") String epub, Model model) {

        model.addAttribute("sortingXhtmlFiles", service.findAllEpubXhtml(Long.parseLong(epub)));
        model.addAttribute("sortingCssFiles", service.findAllEpubCss(Long.parseLong(epub)));
        model.addAttribute("sortingImageFiles", service.findAllEpubImage(Long.parseLong(epub)));

        model.addAttribute("epub", epub);

        return "orderresorces.views";
    }

    @RequestMapping(value = "/orderresources/insertxhtml", method = {RequestMethod.GET})
    public String insertXhtmlStart(@RequestParam("epub") String epub, Model model) {

        model.addAttribute("epub", epub);

        return "uploadresources.xhtml";
    }

    @RequestMapping(value = "/orderresources/insertxhtml", method = {RequestMethod.POST})
    public String insertXhtml(@RequestParam("epub") String epub, @RequestParam("xhtmlfile") MultipartFile xhtmlfile) {

        if (!xhtmlfile.isEmpty()) {
            try {
                byte[] bytes = xhtmlfile.getBytes();
                EpubXhtml epubXhtml = new EpubXhtml(null, FilenameUtils.getBaseName(xhtmlfile.getOriginalFilename()), xhtmlfile.getOriginalFilename(), bytes, 0, "application/xhtml+xml", epub, null);
                int result = service.addXHTML(Long.parseLong(epub), epubXhtml);
                if (result > 0) {

                } else {

                }
            } catch (IOException e) {
                throw new BusinessException("Errore I/O", e);
            }
        }

        return "redirect:/packager/orderresources?epub=" + epub;
    }

    @RequestMapping(value = "/orderresources/insertcss", method = {RequestMethod.GET})
    public String insertCssStart(@RequestParam("epub") String epub, Model model) {

        model.addAttribute("epub", epub);

        return "uploadresources.css";
    }

    @RequestMapping(value = "/orderresources/insertcss", method = {RequestMethod.POST})
    public String insertCss(@RequestParam("epub") String epub, @RequestParam("cssfile") MultipartFile cssfile) {

        if (!cssfile.isEmpty()) {
            try {
                byte[] bytes = cssfile.getBytes();
                EpubCss epubCss = new EpubCss(null, FilenameUtils.getBaseName(cssfile.getOriginalFilename()), cssfile.getOriginalFilename(), bytes, cssfile.getContentType(), epub);
                System.out.println(epubCss.toString());
                int result = service.addStylesheet(Long.parseLong(epub), epubCss);
                if (result > 0) {

                } else {

                }
            } catch (IOException e) {
                throw new BusinessException("Errore I/O", e);
            }
        }

        return "redirect:/packager/orderresources?epub=" + epub;
    }

    @RequestMapping(value = "/orderresources/insertimage", method = {RequestMethod.GET})
    public String insertImageStart(@RequestParam("epub") String epub, Model model) {

        model.addAttribute("epub", epub);

        return "uploadresources.image";
    }

    @RequestMapping(value = "/orderresources/insertimage", method = {RequestMethod.POST})
    public String insertImage(@RequestParam("epub") String epub, @RequestParam("imagefile") MultipartFile imagefile) {

        if (!imagefile.isEmpty()) {
            try {
                byte[] bytes = imagefile.getBytes();
                EpubImage epubImage = new EpubImage(null, FilenameUtils.getBaseName(imagefile.getOriginalFilename()), imagefile.getOriginalFilename(), bytes, imagefile.getContentType(), epub);
                System.out.println(epubImage.toString());
                int result = service.addImage(Long.parseLong(epub), epubImage);
                if (result > 0) {
                    
                } else {
                    
                }
            } catch (IOException e) {
                throw new BusinessException("Errore I/O", e);
            }
        }

        return "redirect:/packager/orderresources?epub=" + epub;
    }

    @RequestMapping(value = "/orderresources/updatexhtml", method = {RequestMethod.GET})
    public String updateOrderXhtmlStart(@RequestParam("id") String id, Model model) {

        EpubXhtml epubXhtml = service.getEpubXhtmlByPk(id);
        model.addAttribute("epubXhtml", epubXhtml);

        return "orderresorces.updatexhtmlform";
    }
    
    @RequestMapping(value = "/orderresources/updatexhtml", method = {RequestMethod.POST})
    public String updateOrderXhtml(@ModelAttribute EpubXhtml epubXhtml) {

        service.updateEpubXhtml(epubXhtml);

        return "redirect:/packager/orderresources?epub=" + epubXhtml.getEpub();
    }

    @RequestMapping(value = "/orderresources/deletexhtml", method = {RequestMethod.GET})
    public String deleteOrderXhtmlStart(@RequestParam("id") String id, Model model) {

        EpubXhtml epubXhtml = service.getEpubXhtmlByPk(id);
        model.addAttribute("epubXhtml", epubXhtml);

        return "orderresorces.deletexhtmlform";
    }

    @RequestMapping(value = "/orderresources/deletexhtml", method = {RequestMethod.POST})
    public String deleteOrderXhtml(@ModelAttribute EpubXhtml epubXhtml) {

        service.deleteEpubXhtml(epubXhtml);

        return "redirect:/packager/orderresources?epub=" + epubXhtml.getEpub();
    }
    
    @RequestMapping(value = "/orderresources/updatecss", method = {RequestMethod.GET})
    public String updateOrderCssStart(@RequestParam("id") String id, Model model) {

        EpubCss epubCss = service.getEpubCssByPk(id);
        model.addAttribute("epubCss", epubCss);

        return "orderresorces.updatecssform";
    }
    
    @RequestMapping(value = "/orderresources/updatecss", method = {RequestMethod.POST})
    public String updateOrderCss(@ModelAttribute EpubCss epubCss) {

        service.updateEpubCss(epubCss);

        return "redirect:/packager/orderresources?epub=" + epubCss.getEpub();
    }

    @RequestMapping(value = "/orderresources/deletecss", method = {RequestMethod.GET})
    public String deleteOrderCssStart(@RequestParam("id") String id, Model model) {

        EpubCss epubCss = service.getEpubCssByPk(id);
        model.addAttribute("epubCss", epubCss);

        return "orderresorces.deletecssform";
    }

    @RequestMapping(value = "/orderresources/deletecss", method = {RequestMethod.POST})
    public String deleteOrderCss(@ModelAttribute EpubCss epubCss) {

        service.deleteEpubCss(epubCss);

        return "redirect:/packager/orderresources?epub=" + epubCss.getEpub();
    }
    
    @RequestMapping(value = "/orderresources/updateimage", method = {RequestMethod.GET})
    public String updateOrderImageStart(@RequestParam("id") String id, Model model) {

        EpubImage epubImage = service.getEpubImageByPk(id);
        model.addAttribute("epubImage", epubImage);

        return "orderresorces.updateimageform";
    }
    
    @RequestMapping(value = "/orderresources/updateimage", method = {RequestMethod.POST})
    public String updateOrderImage(@ModelAttribute EpubImage epubImage) {

        service.updateEpubImage(epubImage);

        return "redirect:/packager/orderresources?epub=" + epubImage.getEpub();
    }

    @RequestMapping(value = "/orderresources/deleteimage", method = {RequestMethod.GET})
    public String deleteOrderImageStart(@RequestParam("id") String id, Model model) {

        EpubImage epubImage = service.getEpubImageByPk(id);
        model.addAttribute("epubImage", epubImage);

        return "orderresorces.deleteimageform";
    }

    @RequestMapping(value = "/orderresources/deleteimage", method = {RequestMethod.POST})
    public String deleteOrderImage(@ModelAttribute EpubImage epubImage) {

        service.deleteEpubImage(epubImage);

        return "redirect:/packager/orderresources?epub=" + epubImage.getEpub();
    }

    @RequestMapping(value = "/orderok", method = {RequestMethod.GET})
    public String orderResources(@RequestParam("epub") String epub, Model model) {

        service.finalizeEpub(Long.parseLong(epub));

        model.addAttribute("epub", epub);
        
        return "download.epub";
    }
    
     /**
     * Gestione del download dell'Epub
     */
    @RequestMapping(value="/downloadEpub", method={RequestMethod.GET})
    public void dowloadEpub(@RequestParam("epub") String epub, HttpServletResponse response) throws BusinessException, FileNotFoundException, IOException {
        String downloadPath = UPLOAD_DIR + epub + File.separator + epub + ".epub";
        File epubToDownlod = new File(downloadPath);
        InputStream input = new FileInputStream(epubToDownlod);
        response.setContentType("application/force-download");
	response.setHeader("Content-Disposition", "attachment; filename=" + epubToDownlod.getName());
        IOUtils.copy(input, response.getOutputStream());
        response.flushBuffer();
    }
}
