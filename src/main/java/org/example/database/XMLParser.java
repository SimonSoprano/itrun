package org.example.database;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;

public class XMLParser {
    public Map<String , String> parseElement(File file,Map<String , Field> keys){
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SaxHandler handler = new SaxHandler(keys);
        SAXParser parser = null;


        try {
             parser = factory.newSAXParser();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }


        try {
            parser.parse(file, handler);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return handler.getResult();

    }
}
