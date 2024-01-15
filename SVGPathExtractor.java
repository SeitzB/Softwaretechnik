

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class SVGPathExtractor {

    public static void main(String[] args) {
        try {
            String svgFilePath = "path/to/your/svg/file.svg"; // Replace with the actual path to your SVG file
            File svgFile = new File(svgFilePath);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(svgFile);

            NodeList pathElements = doc.getElementsByTagName("path");
            for (int i = 0; i < pathElements.getLength(); i++) {
                Element pathElement = (Element) pathElements.item(i);
                String pathData = pathElement.getAttribute("d");
                System.out.println("Path " + (i + 1) + ": " + pathData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}