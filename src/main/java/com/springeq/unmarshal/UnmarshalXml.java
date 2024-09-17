package com.springeq.unmarshal;

import com.example.generated.ObjectFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileInputStream;
import javax.xml.stream.XMLStreamException;
import javax.xml.bind.JAXBException;

public class UnmarshalXml {
    public static void main(String[] args) {
        try {
            // Apply the XSLT transformation
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            StreamSource xslt = new StreamSource(new File("src/main/resources/unmarshal/LDDReport2html_wAppInfo.xsl"));
            Transformer transformer = transformerFactory.newTransformer(xslt);

            // Source XML and output the transformed result to a temporary file
            StreamSource xmlInput = new StreamSource(new File("src/main/resources/unmarshal/LDDReport_v3.4.0.0_B324.xml"));
            File transformedXmlFile = new File("src/main/resources/transformed.xml");
            StreamResult xmlOutput = new StreamResult(transformedXmlFile);

            // Apply transformation from XML to the temporary transformed file
            transformer.transform(xmlInput, xmlOutput);

            // Use StAX to unmarshal large XML files using streaming
            XMLInputFactory xmlInputFactory = XMLInputFactory.newFactory();
            FileInputStream transformedFileStream = new FileInputStream(transformedXmlFile);
            XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(transformedFileStream);

            // Create JAXB context and unmarshaller for the generated ObjectFactory class
            JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            // Process the XML as a stream
            Object parsedObject = unmarshaller.unmarshal(xmlStreamReader);

            // Close the stream reader
            xmlStreamReader.close();

            // Output the result of unmarshalling
            System.out.println("Parsed transformed object: " + parsedObject);

        } catch (JAXBException e) {
            System.err.println("Error during JAXB unmarshalling: " + e.getMessage());
            e.printStackTrace();
        } catch (XMLStreamException e) {
            System.err.println("Error during XML streaming: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("General error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
