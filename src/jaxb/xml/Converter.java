package jaxb.xml;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;


public class Converter {
	public static Object convertFromXmlToObject(Object xmlString, Class... type) throws JAXBException {
        Object result = new Object();

        StringReader sr = null;
        
        if (xmlString instanceof String){
            sr = new StringReader((String)xmlString);
        }
        
        try{
            JAXBContext context = JAXBContext.newInstance(type);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            result =  unmarshaller.unmarshal(sr);
        }catch (JAXBException e) {
    		e.printStackTrace();
        }
        return result;
	}
	
   public static String convertFromObjectToXml(Object source, @SuppressWarnings("rawtypes") Class... type) {        
        String result;
        StringWriter sw = new StringWriter();
        try {
            JAXBContext context     = JAXBContext.newInstance(type);
            Marshaller  marshaller  = context.createMarshaller();
            marshaller.marshal(source, sw);
            result = sw.toString();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        return result;
    }
	
}
