package com.signove.health.servicetest;

import java.io.ByteArrayInputStream;
import java.io.IOException;

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
}
