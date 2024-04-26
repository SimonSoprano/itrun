package org.example.database;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class SaxHandler extends DefaultHandler {

    private Map<String , String > result = new HashMap<>();
    private Map<String , Field > tags ;
    public SaxHandler(Map<String , Field> tags ) {
        this.tags = tags;
    }

    private String currentTagNAme = "";
    public Map<String, String> getResult(){
        return result;
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        currentTagNAme = qName;
        super.startElement(uri, localName, qName, attributes);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        currentTagNAme = null;
        super.endElement(uri, localName, qName);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if(currentTagNAme!=null && tags.containsKey(currentTagNAme)){
            String chars = new String(ch, start, length);
            result.put(currentTagNAme, chars);
        }
        super.characters(ch, start, length);
    }

}
