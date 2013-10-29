package br.ufcg.embedded.health.servicetest;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.util.Log;

public class ParserXML {
    public Document parse_xml(String xml) {
        Document doc = null;

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));
        } catch (ParserConfigurationException e) {
            Log.w("Antidote", "XML parser error");
        } catch (SAXException e) {
            Log.w("Antidote", "SAX exception");
        } catch (IOException e) {
            Log.w("Antidote", "IO exception in xml parsing");
        }

        return doc;
    }

    public String get_xml_text(Node n) {
        String s = null;
        NodeList text = n.getChildNodes();

        for (int l = 0; l < text.getLength(); ++l) {
            Node txt = text.item(l);
            if (txt.getNodeType() == Node.TEXT_NODE) {
                if (s == null) {
                    s = "";
                }
                s += txt.getNodeValue();
            }
        }

        return s;
    }
    
    public List<Double> extractValues(Document doc){
        List<Double> listData = new ArrayList<Double>();
        try {
            //optional, but recommended
            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("entry");

            for (int i = 0; i < nList.getLength(); i++) {
                NodeList nListChildEntry = nList.item(i).getChildNodes();
                for (int j = 0; j < nListChildEntry.getLength(); j++) {
                    Node nNodeChildEntry = nListChildEntry.item(j);
                    if(nNodeChildEntry.getNodeName().equals("meta-data")){
                        NodeList nListChildMetadata = nNodeChildEntry.getChildNodes();
                        for (int k = 0; k < nListChildMetadata.getLength(); k++) {
                            Node nNodeChildMetadata = nListChildMetadata.item(k);
                            if(nNodeChildMetadata.getNodeName().equals("meta")){
                                NodeList nListChildMeta = nNodeChildMetadata.getChildNodes();
                                for (int l = 0; l < nListChildMeta.getLength(); l++) {
                                    if(nListChildMeta.item(l).getNodeValue().equals("mmHg")){
                                        //Find metadata of pressure(mmHg), now go to entry whith datas.
                                        for (int m = j+1; m < nListChildEntry.getLength(); m++) {
                                            Node nNodeChildEntryMetadata = nListChildEntry.item(m);
                                            if(nNodeChildEntryMetadata.getNodeName().equals("compound")){
                                                NodeList nListEntryMetadata = nNodeChildEntryMetadata.getChildNodes();
                                                for (int n = 0; n < nListEntryMetadata.getLength(); n++) {
                                                    Node nNodeChildCompound = nListEntryMetadata.item(n);
                                                    if(nNodeChildCompound.getNodeName().equals("entries")){
                                                        NodeList nListCompound = nNodeChildCompound.getChildNodes();
                                                        for (int o = 0; o < nListCompound.getLength(); o++) {
                                                            Node nNodeChildEntries = nListCompound.item(o);
                                                            if(nNodeChildEntries.getNodeName().equals("entry")){
                                                                NodeList nListEntry = nNodeChildEntries.getChildNodes();
                                                                for (int p = 0; p < nListEntry.getLength(); p++) {
                                                                    Node nNodeChildEntryData = nListEntry.item(p);
                                                                    if(nNodeChildEntryData.getNodeName().equals("simple")){
                                                                        NodeList nListSimple = nNodeChildEntryData.getChildNodes();
                                                                        for (int q = 0; q < nListSimple.getLength(); q++) {
                                                                            Node nNodeChildSimple = nListSimple.item(q);
                                                                            if(nNodeChildSimple.getNodeName().equals("value")){
                                                                                NodeList nListValue = nNodeChildSimple.getChildNodes();
                                                                                for (int r = 0; r < nListValue.getLength(); r++) {
                                                                                    Node nNodeValue = nListValue.item(r);
                                                                                    listData.add(Double.parseDouble(nNodeValue.getNodeValue()));
                                                                                }
                                                                                
                                                                            }
                                                                            
                                                                        }
                                                                        
                                                                    }
                                                                    
                                                                }
                                                                
                                                            }
                                                            
                                                        }
                                                        
                                                    }
                                                    
                                                }
                                                
                                            }
                                            
                                        }
                                        
                                    }
                                    
                                }
                               
                            }
                            
                        }
                        
                    }
                    
                }
                
            }
        
        } catch (Exception e) {
        e.printStackTrace();
        }
        
        return listData;
    }
}
