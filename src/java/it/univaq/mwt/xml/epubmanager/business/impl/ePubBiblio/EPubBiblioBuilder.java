package it.univaq.mwt.xml.epubmanager.business.impl.ePubBiblio;

import it.univaq.mwt.xml.epubmanager.business.model.ePubBiblio.EPub;
import it.univaq.mwt.xml.epubmanager.business.model.ePubBiblio.EPubBiblio;
import it.univaq.mwt.xml.epubmanager.business.model.ePubBiblio.EpubFile;
import it.univaq.mwt.xml.epubmanager.business.model.ePubBiblio.Statistiche;
import it.univaq.mwt.xml.epubmanager.common.ePubBiblioUtility.StatisticUtility;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class EPubBiblioBuilder {
    
     /**
     * Questo metodo serve a creare la biblioteca di ePub.
     *
     * @param nomeBib indica EPubBiblio.xml
     * @param listaEPub è la lista di EPubFile che devono essere inseriti nella
     * biblioteca
     * @param pathB indica pathEPubBiblio ovvero dove salvare la biblioteca
     */
    public static void creaEPubBiblio(String nomeBib, List<EpubFile> listaEPub, String pathB) {

        EPubBiblio ePubBiblio = StatisticUtility.analizza(listaEPub, pathB);
        /*Creiamo un documento con Stax*/
        XMLStreamWriter writer = null;
        OutputStreamWriter headerBib = null;

        try {
            FileOutputStream fos = new FileOutputStream(nomeBib);
            headerBib = new OutputStreamWriter(fos, "UTF-8");
            //Istanziamo XMLOutputFactory
            XMLOutputFactory factoryBiblio = XMLOutputFactory.newInstance();
            //Generiamo un XMLStreamWriter e gli passo lo stream su cui scrivere il documento XML
            writer = factoryBiblio.createXMLStreamWriter(headerBib);

            /* Utilizzo i metodi XMLStreamWriter nell'ordine corretto per creare il documento*/
            writer.writeStartDocument("UTF-8", "1.0");
            writer.writeStartElement("ePubBiblio");
            writer.writeAttribute("xmlns", "http://it.univaq.mwt.xml/epubmanager");
            writer.writeAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            writer.writeAttribute("xsi:schemaLocation", "/EPUBManager/src/main/java/it/univaq/mwt/xml/epunmanager/business/model/ePubBiblio ePubBiblio.xsd");
            Iterator i = ePubBiblio.getePubList().iterator();
            writer.writeStartElement("ePubList");
            //Inserisco nella biblioteca gli ePub richiamando il metodo creaEPub
            while (i.hasNext()) {
                EPub ePub = (EPub) i.next();
                creaEPub(ePub, writer);
            }
            writer.writeEndElement();
            i = ePubBiblio.getePubList().iterator();
            //Inserisco nella biblioteca le statistiche richiamando il metodo creaStatistiche
            creaStatistiche(ePubBiblio.getePubList(), writer);

            writer.writeEndElement();
            writer.writeEndDocument();

        } catch (FileNotFoundException e) {
        } catch (UnsupportedEncodingException | XMLStreamException ex) {
            Logger.getLogger(EPubBiblio.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            //Chiamo il metodo close per chiudere l'XMLStreamWriter
            try {
                if (writer != null) {
                    writer.close();
                }
                if (headerBib != null) {
                    headerBib.close();
                }
                System.out.println("La creazione del file XML della biblioteca è avvenuto con successo");
            } catch (XMLStreamException | IOException e) {
                System.err.println("Errore:" + e.getMessage());
            }
        }
    }


    /**
     * Metodo per la creazione degli EPub nel documento EPubBiblio.xml tramite i
     * metodi Stax di XMLStreamWriter
     *
     * @param ePub
     * @param writer
     */
    private static void creaEPub(EPub ePub, XMLStreamWriter writer) throws XMLStreamException {

        writer.writeStartElement("EPub");
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat datF = new SimpleDateFormat("yyyy-MM-dd");
        String data = datF.format(cal.getTime());
        writer.writeAttribute("id", Integer.toString(ePub.getId()));
        writer.writeAttribute("dataInserimento", data);

        writer.writeStartElement("titolo");
        writer.writeCharacters(ePub.getTitolo());
        writer.writeEndElement();

        writer.writeStartElement("lingua");
        writer.writeCharacters(ePub.getLingua());
        writer.writeEndElement();

        writer.writeStartElement("isbn");
        writer.writeCharacters(ePub.getIsbn());
        writer.writeEndElement();

        if (ePub.getAutore() != null) {
            writer.writeStartElement("autore");
            writer.writeCharacters(ePub.getAutore());
            writer.writeEndElement();
        }
        if (ePub.getEditore() != null) {
            writer.writeStartElement("editore");
            writer.writeCharacters(ePub.getEditore());
            writer.writeEndElement();
        }
        if (ePub.getFormato() != null) {
            writer.writeStartElement("formato");
            writer.writeCharacters(ePub.getFormato());
            writer.writeEndElement();
        }
        if (ePub.getOrigine() != null) {
            writer.writeStartElement("origine");
            writer.writeCharacters(ePub.getOrigine());
            writer.writeEndElement();
        }
        if (ePub.getTipo() != null) {
            writer.writeStartElement("tipo");
            writer.writeCharacters(ePub.getTipo());
            writer.writeEndElement();
        }
        if (ePub.getData() != null) {
            writer.writeStartElement("data");
            writer.writeCharacters(ePub.getData());
            writer.writeEndElement();
        }
        if (ePub.getCollaboratore() != null) {
            writer.writeStartElement("collaboratore");
            writer.writeCharacters(ePub.getCollaboratore());
            writer.writeEndElement();
        }
        if (ePub.getRelazione() != null) {
            writer.writeStartElement("relazione");
            writer.writeCharacters(ePub.getRelazione());
            writer.writeEndElement();
        }
        if (ePub.getDirittiAutore() != null) {
            writer.writeStartElement("dirittiAutore");
            writer.writeCharacters(ePub.getDirittiAutore());
            writer.writeEndElement();
        }
        if (ePub.getDescrizione() != null) {
            writer.writeStartElement("descrizione");
            writer.writeCharacters(ePub.getDescrizione());
            writer.writeEndElement();
        }
        if (ePub.getDestinazione() != null) {
            writer.writeStartElement("destinazione");
            writer.writeCharacters(ePub.getDestinazione());
            writer.writeEndElement();
        }
        if (ePub.getSoggetto() != null) {
            writer.writeStartElement("soggetto");
            writer.writeCharacters(ePub.getSoggetto());
            writer.writeEndElement();
        }

        writer.writeStartElement("immagini");
        writer.writeCharacters(Integer.toString(ePub.getImmagini()));
        writer.writeEndElement();

        writer.writeStartElement("parole");
        writer.writeCharacters(Integer.toString(ePub.getParole()));
        writer.writeEndElement();

        writer.writeStartElement("capitoli");
        writer.writeCharacters(Integer.toString(ePub.getCapitoli()));
        writer.writeEndElement();

        writer.writeStartElement("percorsofile");
        writer.writeCharacters(ePub.getPathFile());
        writer.writeEndElement();

        writer.writeEndElement();
    }

    /**
     * Metodo per la creazione della parte relativa alle statistiche nel
     * documento EPubBiblio.xml tramite i metodi Stax di XMLStreamWriter
     *
     * @param ePubList
     * @param writer
     */
    private static void creaStatistiche(List<EPub> ePubList, XMLStreamWriter writer) throws XMLStreamException {

        writer.writeStartElement("lista-statistiche");
        Map<String, Map<String, Integer>> statistiche = Statistiche.mappaStatistiche(ePubList);
        Map<String, Integer> autori = statistiche.get("autori");
        if (autori != null) {
            writer.writeStartElement("StatisticheAutori");
            Iterator it = autori.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry p = (Map.Entry) it.next();
                System.out.println(p.getKey() + "=" + p.getValue());
                writer.writeStartElement("author");
                writer.writeStartElement("nome");
                writer.writeCharacters((String) p.getKey());
                writer.writeEndElement();
                writer.writeStartElement("frequenza");
                Integer j = (Integer) p.getValue();
                writer.writeCharacters(j.toString());
                writer.writeEndElement();
                writer.writeEndElement();
                it.remove();
            }
            writer.writeEndElement();
        }
        writer.writeEndElement();
    }

    /**
     * Metodo per aggiornare la parte delle statistiche nel documento
     * EPubBiblio.xml
     *
     * @param doc
     * @param ePub
     */
    private static void aggiornaStatistiche(Document doc, List<EPub> ePub) {
        //Qui aggiorniamo l'elemento Statistica
        for (Iterator iterator = ePub.iterator(); iterator.hasNext();) {
            EPub ep = (EPub) iterator.next();
            //Espressione per vedere se il nome del nodo author è uguale al nome dell'autore dell'EPub i-esimo
            String expression = "//lista-statistiche/StatisticheAutori/author[nome=\"" + ep.getAutore() + "\"]";
            /*XPath*/
            XPathFactory fact = XPathFactory.newInstance();
            XPath xPath = fact.newXPath();
            NodeList XMeta;
            try {
                /*
                 *Il metodo compile di XPath compila un'espressione XPath usata più volte 
                 * e si applica usando il metodo evaluate dell'oggetto XPathExpresson così ottenuto
                 */
                XPathExpression expr = xPath.compile(expression);
                XMeta = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
                //prendo la lista di nodi author
                NodeList temp = doc.getElementsByTagName("author");

                //Se esiste un nodo author con l'elemento nome uguale all'autore dell'EPub appena inserito vado ad aggiornare la frequenza
                if (XMeta.getLength() != 0) {

                    Element autore = (Element) XMeta.item(0);
                    String nomeAutore = autore.getElementsByTagName("nome").item(0).getTextContent();
                    boolean trovato = false;
                    for (int i = 0; i < temp.getLength() - 1 || !trovato; i++) {

                        Element author = (Element) temp.item(i);
                        String newNomeAutore = author.getElementsByTagName("nome").item(0).getTextContent();

                        if (newNomeAutore == nomeAutore) {
                            if (!author.getElementsByTagName("frequenza").item(0).getTextContent().equals(null)) {
                                int x = Integer.parseInt(author.getElementsByTagName("frequenza").item(0).getTextContent());
                                x++;
                                author.getElementsByTagName("frequenza").item(0).setTextContent(Integer.toString(x));
                                trovato = true;
                            }
                        }
                    }
                } else {
                    //Altrimenti non ci sono altri ePub con quell'autore
                    //Creo un nuovo elemento author con i relativi figli e li setto
                    Element nuovoAut = doc.createElement("author");
                    Element nome = doc.createElement("nome");
                    nome.setTextContent(ep.getAutore());
                    nuovoAut.appendChild(nome);
                    Element frequenza = doc.createElement("frequenza");
                    frequenza.setTextContent(Integer.toString(1));
                    nuovoAut.appendChild(frequenza);
                    temp.item(0).getParentNode().appendChild(nuovoAut);
                }

            } catch (XPathExpressionException e) {
                System.err.println("Errore:" + e.getMessage());
            }
            //fine aggiornamento parte statistiche}
        }
    }

    /**
     * Metodo per aggiornare EPubBiblio.xml
     *
     * @param nomeBib
     * @param ePubList
     */
    public static void aggiornaEPubBiblio(String nomeBib, List<EPub> ePubList) {
        /*DOM*/
        String path = nomeBib;
        File ePubBiblio = new File(path);
        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        DocumentBuilder docB;
        try {
            docB = f.newDocumentBuilder();
            Document doc = docB.parse(ePubBiblio);
            doc.getDocumentElement().normalize();
            //Richiamo i metodi per aggiungere dli elementi EPub e per aggiornare le statistiche
            aggiungiEPubElement(doc, ePubList);
            aggiornaStatistiche(doc, ePubList);

            doc.getDocumentElement().normalize();
            /* Trasformazione XSLT */
            //Creo un'istanza di TransformerFactory
            TransformerFactory tFactory = TransformerFactory.newInstance();
            //Genero il Transformer dalla TransformerFactory associandoci la risorsa XSLT tramite l'oggetto source
            Transformer transformer = tFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(path));

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            //Chiamo il metodo transform sul transformer passando il documento da passare tramite la source  
            //e il risultato da generare come result
            transformer.transform(source, result);
            System.out.println("L'aggiornamento del file XML della biblioteca è avvenuto con successo");

        } catch (SAXException | ParserConfigurationException | IOException | TransformerException e) {
            System.err.println("Errore:" + e.getMessage());
        }
    }

    /**
     * Metodo per aggiungere al file EPubBiblio.xml gli elementi EPub in base
     * alla lista di EPub da inserire
     *
     * @param doc
     * @param ePub
     */
    private static void aggiungiEPubElement(Document doc, List<EPub> ePub) {
        String rPath = doc.getDocumentURI();
        String rp1 = rPath.replace("file:/", "");
        String rp2 = rp1.replace("/", File.separator);
        int id = StatisticUtility.addID(rp2);

        for (Iterator iterator = ePub.iterator(); iterator.hasNext();) {
            EPub ep = (EPub) iterator.next();
            Element radice = doc.createElement("EPub");
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat datF = new SimpleDateFormat("yyyy-MM-dd");
            String dataX = datF.format(cal.getTime());

            radice.setAttribute("dataInserimento", dataX);//è questo l'attributo

            radice.setAttribute("id", Integer.toString(id));
            id++;
            if (ep.getTitolo() != null) {
                Element titolo = doc.createElement("titolo");
                titolo.setTextContent(ep.getTitolo());
                radice.appendChild(titolo);
            }

            if (ep.getLingua() != null) {

                Element lingua = doc.createElement("lingua");
                lingua.setTextContent(ep.getLingua());
                radice.appendChild(lingua);

            }
            if (ep.getIsbn() != null) {
                Element isbn = doc.createElement("isbn");
                isbn.setTextContent(ep.getIsbn());
                radice.appendChild(isbn);
            }
            if (ep.getAutore() != null) {
                Element autore = doc.createElement("autore");
                autore.setTextContent(ep.getAutore());
                radice.appendChild(autore);
            }

            if (ep.getEditore() != null) {

                Element editore = doc.createElement("editore");
                editore.setTextContent(ep.getEditore());
                radice.appendChild(editore);
            }
            if (ep.getFormato() != null) {

                Element formato = doc.createElement("formato");
                formato.setTextContent(ep.getFormato());
                radice.appendChild(formato);
            }
            if (ep.getOrigine() != null) {
                Element origine = doc.createElement("origine");
                origine.setTextContent(ep.getOrigine());
                radice.appendChild(origine);
            }
            if (ep.getTipo() != null) {

                Element tipo = doc.createElement("tipo");
                tipo.setTextContent(ep.getTipo());
                radice.appendChild(tipo);
            }
            if (ep.getData() != null) {

                Element data = doc.createElement("data");
                data.setTextContent(ep.getData());
                radice.appendChild(data);

            }
            if (ep.getCollaboratore() != null) {

                Element collaboratore = doc.createElement("collaboratore");
                collaboratore.setTextContent(ep.getCollaboratore());
                radice.appendChild(collaboratore);
            }
            if (ep.getRelazione() != null) {

                Element relazione = doc.createElement("relazione");
                relazione.setTextContent(ep.getRelazione());
                radice.appendChild(relazione);
            }
            if (ep.getDirittiAutore() != null) {

                Element dirittiAutore = doc.createElement("dirittiAutore");
                dirittiAutore.setTextContent(ep.getDirittiAutore());
                radice.appendChild(dirittiAutore);
            }
            if (ep.getDestinazione() != null) {

                Element destinazione = doc.createElement("destinazione");
                destinazione.setTextContent(ep.getDestinazione());
                radice.appendChild(destinazione);
            }
            if (ep.getDescrizione() != null) {

                Element descrizione = doc.createElement("descrizione");
                descrizione.setTextContent(ep.getDescrizione());
                radice.appendChild(descrizione);
            }
            if (ep.getSoggetto() != null) {

                Element soggetto = doc.createElement("soggetto");
                soggetto.setTextContent(ep.getSoggetto());
                radice.appendChild(soggetto);
            }

            Element immagini = doc.createElement("immagini");
            immagini.setTextContent(Integer.toString(ep.getImmagini()));
            radice.appendChild(immagini);

            Element parole = doc.createElement("parole");
            parole.setTextContent(Integer.toString(ep.getParole()));
            radice.appendChild(parole);

            Element capitoli = doc.createElement("capitoli");
            capitoli.setTextContent(Integer.toString(ep.getCapitoli()));
            radice.appendChild(capitoli);

            Element percorso = doc.createElement("percorsofile");
            percorso.setTextContent(ep.getPathFile());
            radice.appendChild(percorso);

            Node posizioneCorretta = doc.getElementsByTagName("ePubList").item(0);

            posizioneCorretta.appendChild(radice);
        }

    }


}
