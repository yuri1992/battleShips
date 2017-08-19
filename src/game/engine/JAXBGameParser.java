package game.engine;

import java.io.File;
import java.io.FileNotFoundException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class JAXBGameParser {

    public static GameManager loadGameFromXML(String fileName) throws FileNotFoundException, FileNotXmlFormat, JAXBException {
        File f = new File(fileName);
        return loadGameFormFile(f);
    }

    public static void validateFile(File f) throws FileNotFoundException, FileNotXmlFormat {
        if (!f.exists()) {
            throw new FileNotFoundException(f.getName() + " Not Found.");
        }

        if (!f.isFile() || !f.getPath().endsWith(".xml")) {
            throw new FileNotXmlFormat();
        }
    }

    public static GameManager loadGameFormFile(File file) throws FileNotFoundException, FileNotXmlFormat, JAXBException {
        validateFile(file);
        JAXBContext jaxbContext = JAXBContext.newInstance(GameManager.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        GameManager game = (GameManager) jaxbUnmarshaller.unmarshal(file);
        return game;
    }
}

